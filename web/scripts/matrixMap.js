//Processed data to use
let mtrx_ont1root;
let mtrx_ont2root;
let mtrx_alignments;

const cw = 20; //cell width
const cellRect = (x,y) => `M${x},${y} v${cw} h${cw} v-${cw} Z`;
const cellTriangle = (x,y) => `M${x},${y} v${cw} l${cw},-${cw} Z`;
const cellShape = d => {
    const x = d.e2pos.y;
    const y = d.e1pos.y;
    const rectangle = `M${x},${y} v${cw} h${cw} v-${cw} Z`;
    const triangle = `M${x},${y} v${cw} l${cw},-${cw} Z`;
    return d.overlappedTop ? triangle : rectangle;
};

/**
 * Creates matrixMapping and returns svg:g
 * @param {*} ont1root 
 * @param {*} ont2root 
 * @param {*} alignments 
 */
function matrixMapping(ont1root, ont2root, alignments) {
    console.log(`matrixMapping(ont1root, ont2root, alignments)`);

    //Creates a detached g to return
    const g = d3.create("svg:g");
}

function setDataset(dataset) {
    //Builds proper structures for ontologies and alignments
    const ont1TreeRoot = tree( hierarchy(dataset.ont1.root), 'right');
    const ont2TreeRoot = tree( hierarchy(dataset.ont2.root), 'left');
    const newAlignments = buildNewAlignments(dataset.maps.alignments, ont1TreeRoot, ont2TreeRoot);
    //Adds a mapping field to a node in tree if there's an alignment for it.
    ont1TreeRoot.each(d => {
        const filtered = newAlignments.filter(a => a.e1.id === d.id);
        if (filtered.length) {
            d.mappings = filtered;
        }
    });
    ont2TreeRoot.each(d => {
        const filtered = newAlignments.filter(a => a.e2.id === d.id);
        if (filtered.length) {
            d.mappings = filtered;
        }
    });
    //Adds a function to each elements which fetch all exisiting mappings of its descendants
    ont1TreeRoot.each(d => {
        d.mappingsOfDescendants = getAllDescendantMappings(d);
    });
    ont2TreeRoot.each(d => {
        d.mappingsOfDescendants = getAllDescendantMappings(d);
    });

    mtrx_ont1root = ont1TreeRoot;
    mtrx_ont2root = ont2TreeRoot;
    mtrx_alignments = newAlignments;
}


// const dataset_edas_ekaw = {
//     domain: 'conference',
//     ont1: edas,
//     ont2: ekaw,
//     maps: mapping_edas_ekaw
// };

// setDataset(dataset_edas_ekaw);
window.addEventListener('load', function() {
    console.log("window loaded.");
    drawMatrixSvg();
});

/**
 * Draws matrix mapping svg
 */
function drawMatrixSvg()
{
    console.log("drawMatrixSvg()");
    
    const svg = d3.select("#matrix-svg")
        .attr('width', 2300).attr('height', 2300);

    const gPos = {x: treeWidth+10, y: treeWidth-70};
    const g = svg.append('g')
        .attr('transform',`translate(${gPos.x},${gPos.y})`);

    //For fixed header effect
    const headSize = 600;
    const gContent = g.append('g').attr('id','gContent');
    const gRowHead = g.append('g')
        .attr('id','gRowHead').classed('matrix-head', true);
    gRowHead.append('rect').attr('x', -headSize)
        .attr('width', headSize).attr('height', 2300);
    const gColHead = g.append('g')
        .attr('id','gColHead').classed('matrix-head', true);
    gColHead.append('rect').attr('y', -headSize)
        .attr('width',1950).attr('height', headSize);
    const gAnchorHead = g.append('g')
        .attr('id','gAnchorHead').classed('matrix-head', true);
    gAnchorHead.append('rect')
        .attr('x', -headSize).attr('y', -headSize)
        .attr('width', headSize).attr('height', headSize);
    gAnchorHead.append('path')
        .attr('class', 'frozen-shadow horizontal')
        .attr('d', 'M-600,0 H1000')
        .attr('visibility', 'hidden')
        .clone()
            .attr('class', 'frozen-shadow vertical')
            .attr('d', 'M0,-600 V1000');


    //Adds ontology treecharts
    const hGap = nodeHeight/2; //gap between headers and matrix
    const gTree1 = gRowHead.append(() => treechart(mtrx_ont1root, "right"))
        .attr('id','gTree1')
        .attr('transform',`translate(${-hGap},${hGap})`)
        .classed('right-aligned', true);
    const gTree2 = gColHead.append(() => treechart(mtrx_ont2root, "left"))
        .attr('id','gTree2')
        .attr('transform',`translate(${hGap},${-hGap}), rotate(270)`)
        .classed('horizontal', true);
    
    //Draws matrix background table rows and columns
    const cellSize = nodeHeight;
    let row = mtrx_ont1root.descendants().length;
    let col = mtrx_ont2root.descendants().length;
    let gGrid = gContent.append(() => grid(row, col, cellSize, true))
        .attr('id','gGrid').classed('bg-grid', true);

    //Draws mapping cells
    console.log('draw matrix mapping');
    const gMap = gContent.append('g')
        .attr('id','gMap');
    
    function update() {
        console.log('update mapCells');

        updateMappingPos(mtrx_alignments);

        const t = gMap.transition().duration(100);

        const mapcell = gMap.selectAll('path')
            .data(mtrx_alignments, d => d.id);
        mapcell.join(
            enter => enter.append('path')
                .classed('map-to-hidden', d => d.mapToHidden)
                .attr('d', cellShape),
            update => update
                .classed('map-to-hidden', d => d.mapToHidden)
                .transition(t)
                    .attr('d', cellShape)
            )
            .attr('id', d => `a${d.id}`)
            .classed('mapping', true)
            .classed('mapCell', true)
            .attr('width', cellSize)
            .attr('height', cellSize)
            .on('mouseover', almt => {
                showCellGuide(almt, mtrx_alignments);
                highlightAlignment(almt, g, mtrx_alignments);
            })
            .on('mouseout', () => {
                unhighlightAll(g);
                gGrid.selectAll('.mapCell-guide').remove();
            });
        //Always place direct mappings on top.
        gMap.selectAll('.map-to-hidden').lower();

        //Highlights alignments for mouse events on tree nodes
        g.selectAll('.node')
            .filter(d => d.mappingsOfDescendants)
            .on('mouseover', d => {
                mappings = d.collapsed ? d.mappingsOfDescendants : d.mappings;
                if(mappings) {
                    showCellGuide(mappings, mtrx_alignments);
                    highlightAlignment(mappings, g, mtrx_alignments);
                }
            })
            .on('mouseout', () => {
                unhighlightAll(g);
                gGrid.selectAll('.mapCell-guide').remove();
            });

        //Updates the grid in the matrix background
        row = mtrx_ont1root.descendants().length;
        col = mtrx_ont2root.descendants().length;
        gContent.select('#gGrid').remove();
        gGrid = gContent.append(() => grid(row, col, cellSize, true))
            .attr('id','gGrid').classed('bg-grid', true)
            .lower();

        // console.log(mtrx_alignments);
    }

    update();
    //Redraws mapping cells
    gTree1.on('click', () => update());
    gTree2.on('click', () => update());

    function showCellGuide(alignments, alignmentSet) {
        if (!alignments) { return; }    //for undefined
        console.log('draw cell guide');
        //Removes any pre-drawn cellGuides
        gGrid.selectAll('.mapCell-guide').remove();
        
        //Includes any alignment sets mapped to redundant nodes
        alignments = Array.isArray(alignments) ? alignments : [alignments];
        const allAlignments = alignments;
        for(let almt of alignments) {
            //Adds additional redundant alignment except itself
            filtered = alignmentSet.filter(d => (d.namePair === almt.namePair) && (d === almt));
            allAlignments.concat(filtered);
        }

        //Draws guide rect to its mapped cell
        for(let almt of allAlignments) {
            const gCellGuide = gGrid.append('g')
                .classed('mapCell-guide', true).raise();
            gCellGuide.append('rect')
                .attr('x', 0).attr('y', almt.e1pos.y)
                .attr('width', cellSize * col).attr('height', cellSize);
            gCellGuide.append('rect')
                .attr('x', almt.e2pos.y).attr('y', 0)
                .attr('width', cellSize).attr('height', cellSize * row);
        }
    }

    //Freezes headers on svg on 'scroll' event
    document.getElementById('matrix-svgdiv')
        .addEventListener('scroll', (e) => {
            const top = e.target.scrollTop;
            const left = e.target.scrollLeft;
            // console.log(`scroll event on matrix. top:${top} left:${left}`);
            //Moves the headers along
            gAnchorHead.attr('transform', `translate(${left},${top})`);
            gRowHead.attr('transform', `translate(${left},0)`);
            gColHead.attr('transform', `translate(0,${top})`);

            //Shows shadow under frozen headers
            gAnchorHead.selectAll('.frozen-shadow.horizontal')
                .attr('visibility', top ? 'visible' : 'hidden');
            gAnchorHead.selectAll('.frozen-shadow.vertical')
                .attr('visibility', left ? 'visible' : 'hidden');
        });
}

/**
 * Draws the table lines
 * @param {number} row 
 * @param {number} col 
 * @param {number} cellSize 
 * @param {boolean} drawsBg 
 */
function grid(row, col, cellSize, drawsBg)
{
    console.log('create background table lines.');
    const g = d3.create('svg:g');

    if (drawsBg) {
        g.append('rect')
            .classed('bg-rect', true)
            .attr('height', row * cellSize)
            .attr('width', col * cellSize);
    }

    const rowG = g.append('g').classed('row', true);
    for (let i=0; i<row+1; i++) {
        rowG.append('line')
            .attr('x1', 0)
            .attr('y1', i * cellSize)
            .attr('x2', col * cellSize)
            .attr('y2', i * cellSize);
    }

    const colG = g.append('g').classed('col', true);
    for (let i=0; i<col+1; i++) {
        colG.append('line')
            .attr('x1', i * cellSize)
            .attr('y1', 0)
            .attr('x2', i * cellSize)
            .attr('y2', row * cellSize);
    }

    return g.node();
}
