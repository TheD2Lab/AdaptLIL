//Processed data to use
let base_ont1root;
let base_ont2root;
let base_alignments;

//To make highlight stay on mouse click on mapping line.
let maplineClicked = false;

/**
 * Creates baselineMapping and returns svg:g
 * @param {*} ont1root 
 * @param {*} ont2root 
 * @param {*} alignments 
 */
function baselineMapping(ont1root, ont2root, alignments) {
    console.log(`baselineMappingVis(ont1root, ont2root, alignments)`);

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

    base_ont1root = ont1TreeRoot;
    base_ont2root = ont2TreeRoot;
    base_alignments = newAlignments;
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
    drawBaselineSvg();
});

/**
 * Draws baseline mapping svg
 */
function drawBaselineSvg()
{
    console.log("drawBaselineSvg()");
    const ontGap = 200;
    
    const svg = d3.select("#baseline-svg")
        .attr('height', 2100)   //TODO: calculate auto
        .attr('width', 1050);   //TODO: adjust to the same width of svgdiv
    const svgWidth = +svg.attr('width');
    // //auto adjust the svg height
    // let newHeight = nodeHeight * Math.max(ont1TreeRoot.count(), ont2TreeRoot.count());
    // svg.attr('height', newHeight);
    const g = svg.append('g')
        .attr('transform',`translate(${svgWidth/2},20)`);
    const gTree1 = g.append(() => treechart(base_ont1root, "right"))
        .attr('id','gTree1')
        .classed('right-aligned', true)
        .attr('transform',`translate(${-ontGap/2},0)`);
    const gTree2 = g.append(() => treechart(base_ont2root, "left"))
        .attr('id','gTree2')
        .attr('transform',`translate(${ontGap/2},0)`);
    const gMap = g.append('g')
        .attr('id','gMap')
        .attr('transform',`translate(${-ontGap/2},0)`); //to center
    
    console.log('draw baseline mapping');

    function update() {
        console.log('update mapLines');

        updateMappingPos(base_alignments);

        const t = gMap.transition().duration(100);

        const mapline = gMap.selectAll('g')
            .data(base_alignments, d => d.id);
        const maplineEnter = mapline.enter()
            .append('g')
                .attr('id', d => `a${d.id}`)
                .classed('mapping', true).classed('mapLine', true);
        maplineEnter
            .on('click', almt => {
                console.log('mapLine clicked!');
                maplineClicked = true;
                highlightAlignment(almt, g, base_alignments);
            })
            .on('mouseover', almt => {
                if (!maplineClicked) {
                    highlightAlignment(almt, g, base_alignments);
                }
            })
            .on('mouseout', () => {
                if (!maplineClicked) unhighlightAll(g);
            });
        maplineEnter.append('path')   //foreground path
            .attr('d', (d,i) => calcMapLinePath(d,i))
            .attr('fill', 'none')
            .attr('class', 'mapLine-fg')
            .clone(true).lower() //background path
                .attr('class', 'mapLine-bg')
            .clone(true).lower() //path select helper
                .attr('class', 'mapLine-select-helper');
        const maplineUpdate = mapline.merge(maplineEnter)
            .classed('map-to-hidden', d => d.mapToHidden)
            .transition(t)
                .each( (d,i,n) => {
                    d3.select(n[i]).selectAll('path')
                        .attr('d', () => calcMapLinePath(d,i));
                });
        //Always place direct mappings on top.
        gMap.selectAll('.map-to-hidden').lower();

        const maplineExit = mapline.exit().transition(t).remove();
        
        //Highlights alignments for mouse events on tree nodes
        g.selectAll('.node')
            .filter(d => d.mappingsOfDescendants)
            .on('mouseover', d => {
                if (!maplineClicked) {
                    mappings = d.collapsed ? d.mappingsOfDescendants : d.mappings;
                    if(mappings) {
                        highlightAlignment(mappings, g, base_alignments);
                    }
                }
            })
            .on('mouseout', () => {
                if (!maplineClicked) unhighlightAll(g);
            });

        // console.log(base_alignments);
    }

    update();
    //Redraws mapping lines
    gTree1.on('click', () => update());
    gTree2.on('click', () => update());

    
    //Turns off the highlight when clicked on other part in svg
    document.getElementById('baseline-svg')
        .addEventListener('click', (e) =>{
            const isMapLineTargeted = d3.select(e.target.parentNode).classed('mapLine');
            if(maplineClicked && !isMapLineTargeted) {
                console.log('baseline svg clicked! Turning off the highlight.');
                maplineClicked = false;
                unhighlightAll(g);
            }
        });
    
    /**
     * Calculates the svg:path for a baseline mapping line
     * @param {Object} almt an alignment mapping
     * @param {number} i the index of the alignment
     */
    function calcMapLinePath(almt, i)
    {
        if (!almt) { return ``; }
        // console.log(`calcMapLinePath() id:${almt.id} e1pos:${almt.e1pos.x},${almt.e1pos.y} e2pos:${almt.e2pos.x},${almt.e2pos.y}`);

        const ontGap = 200;
        const dn = 6; //distance from the nodemark
        const x1 = almt.e1pos.x + dn,
            y1 = almt.e1pos.y,
            x2 = almt.e2pos.x + ontGap - dn,
            y2 = almt.e2pos.y;
        const c = 10,   //curve value
            gm = 20, //margin from ontGap
            hgap = ((ontGap-gm*2) / base_alignments.length).toFixed(0);
        const hx = hgap * i + gm;
        const vy = y2 > y1 ? y2-c : y2+c;
        const cy = y2 > y1 ? c : -c;
        if (y1 == y2) { return `M${x1},${y1} H${x2}`; } //return straight line
        return `M${x1},${y1} H${hx} s${c},0,${c},${cy} V${vy} s0,${cy},${c},${cy} H${x2}`;
    }
}

