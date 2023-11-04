//Processed data to use
let mtrx_ont1root;
let mtrx_ont2root;
let mtrx_alignments;



class MatrixMap {
    constructor(indentedTree) {
        this.indentedTree = indentedTree;

        this.cw = 20; //cell width
        this.cellRect = (x,y) => `M${x},${y} v${this.cw} h${this.cw} v-${this.cw} Z`;
        this.cellTriangle = (x,y) => `M${x},${y} v${this.cw} l${this.cw},-${this.cw} Z`;
        this.cellShape = d => {
            const x = d.e2pos.y;
            const y = d.e1pos.y;
            const rectangle = `M${x},${y} v${this.cw} h${this.cw} v-${this.cw} Z`;
            const triangle = `M${x},${y} v${this.cw} l${this.cw},-${this.cw} Z`;
            return d.overlappedTop ? triangle : rectangle;
        };
    }
    /**
     * Creates matrixMapping and returns svg:g
     * @param {*} ont1root
     * @param {*} ont2root
     * @param {*} alignments
     */
    matrixMapping(ont1root, ont2root, alignments) {
        console.log(`matrixMapping(ont1root, ont2root, alignments)`);

        //Creates a detached g to return
        const g = d3.create("svg:g");
    }

    /**
     * Tooltip shown via row,col of matrix map.
     * @param gridCell
     * @param row
     * @param col
     */
    showTooltip(gridCell, row, col) {
        console.log(gridCell)
        const rowNode = this.indentedTree.hierarchies[0][row];
        const colNode = this.indentedTree.hierarchies[1][col];
        let rowAxisName = rowNode.data.name; //How do we get the row name form the ontology on left???
        let colAxisName = colNode.data.name; //how do we get the col name from the ontology on the right???

        if (rowNode.data._children > 1)
            rowAxisName = '<b>'+rowAxisName+'</b>';
        if (colNode.data._children > 1)
            colAxisName = '<b>'+colAxisName+'</b>';
        //Note, row should correspond to a singular indented tree and so should col
        //if either row or col has Many children, we may want to show a different visualization.
        d3.select('.bg-grid').append('text').attr('left-node-name', rowAxisName)
            .attr('right-node-name', colAxisName)
            .attr("x", gridCell.node().getBBox().x).attr('y', gridCell.node().getBBox().y)
            .text(
            rowAxisName + ' , ' + colAxisName
        ).style('color', 'black').style('fill', 'black').style('stroke', 'black');
    }

    hideAllTooltips() {
        d3.selectAll('.bg-grid>text').remove()
    }

    hideTooltip(gridCell, row, col) {
        const rowNode = this.indentedTree.hierarchies[0][row];
        const colNode = this.indentedTree.hierarchies[1][col];
        let rowAxisName = rowNode.data.name; //How do we get the row name form the ontology on left???
        let colAxisName = colNode.data.name; //how do we get the col name from the ontology on the right???

        d3.select('.bg-grid>text[left-node-name="'+rowAxisName+'"][right-node-name="'+colAxisName+'"]').remove()
    }


    setDataset(dataset) {
        //Builds proper structures for ontologies and alignments
        const ont1TreeRoot = this.indentedTree.constructTree(this.indentedTree.createHierarchy(dataset.ont1.root), 'right');
        const ont2TreeRoot = this.indentedTree.constructTree(this.indentedTree.createHierarchy(dataset.ont2.root), 'left');
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

    /**
     * Draws matrix mapping svg
     */
    drawMatrixSvg() {
        console.log("drawMatrixSvg()");
        const _this = this;
        this.svg = d3.select("#matrix-svg")
            .attr('width', 2300).attr('height', 2300);

        const gPos = {x: this.indentedTree.treeWidth + 10, y: this.indentedTree.treeWidth - 70};
        const g = this.svg.append('g')
            .attr('transform', `translate(${gPos.x},${gPos.y})`);

        //For fixed header effect
        const headSize = 600;
        const gContent = g.append('g').attr('id', 'gContent');
        const gRowHead = g.append('g')
            .attr('id', 'gRowHead').classed('matrix-head', true);
        gRowHead.append('rect').attr('x', -headSize)
            .attr('width', headSize).attr('height', 2300);
        const gColHead = g.append('g')
            .attr('id', 'gColHead').classed('matrix-head', true);
        gColHead.append('rect').attr('y', -headSize)
            .attr('width', 1950).attr('height', headSize);
        const gAnchorHead = g.append('g')
            .attr('id', 'gAnchorHead').classed('matrix-head', true);
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
        const hGap = this.indentedTree.nodeHeight / 2; //gap between headers and matrix
        const gTree1 = gRowHead.append(() => this.indentedTree.treechart(mtrx_ont1root, "right"))
            .attr('id', 'gTree1')
            .attr('transform', `translate(${-hGap},${hGap})`)
            .classed('right-aligned', true);
        const gTree2 = gColHead.append(() => this.indentedTree.treechart(mtrx_ont2root, "left"))
            .attr('id', 'gTree2')
            .attr('transform', `translate(${hGap},${-hGap}), rotate(270)`)
            .classed('horizontal', true);

        //Draws matrix background table rows and columns
        const cellSize = this.indentedTree.nodeHeight;
        let row = mtrx_ont1root.descendants().length;
        let col = mtrx_ont2root.descendants().length;
        let gGrid = gContent.append(() => this.grid(row, col, cellSize, true))
            .attr('id', 'gGrid').classed('bg-grid', true);

        //Draws mapping cells
        console.log('draw matrix mapping');
        const gMap = gContent.append('g')
            .attr('id', 'gMap');

        function update() {
            console.log('update mapCells');

            updateMappingPos(mtrx_alignments);

            const t = gMap.transition().duration(100);

            const mapcell = gMap.selectAll('path')
                .data(mtrx_alignments, d => d.id);
            mapcell.join(
                enter => enter.append('path')
                    .classed('map-to-hidden', d => d.mapToHidden)
                    .attr('d', _this.cellShape),
                update => update
                    .classed('map-to-hidden', d => d.mapToHidden)
                    .transition(t)
                    .attr('d', _this.cellShape)
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
                .on('mouseout', function(d) {
                    unhighlightAll(g);
                    gGrid.selectAll('.mapCell-guide').remove();
                    _this.hideTooltip(g, d.e1Index, d.e2Index)
                })
                .on('mouseover', function(d, i , e) {
                    console.log(g);
                    console.log('^^^g above, almt below vvv')
                    console.log(d)
                    // debugger;
                    //test for inbounds checking.
                    mapWorld.getIntersection({x: this.getBoundingClientRect().x + 3, y: this.getBoundingClientRect().y + 3}, [this])
                    // _this.showTooltip(d3.select(this), d.e1Index, d.e2Index)
                })
            ;
            //Always place direct mappings on top.
            gMap.selectAll('.map-to-hidden').lower();

            //Highlights alignments for mouse events on tree nodes
            g.selectAll('.node')
                .filter(d => d.mappingsOfDescendants)
                .on('mouseover', d => {
                    const mappings = d.collapsed ? d.mappingsOfDescendants : d.mappings;
                    if (mappings) {
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
            gGrid = gContent.append(() => _this.grid(row, col, cellSize, true))
                .attr('id', 'gGrid').classed('bg-grid', true)
                .lower();

            // console.log(mtrx_alignments);
        }

        update();
        //Redraws mapping cells
        gTree1.on('click', () => update());
        gTree2.on('click', () => update());

        function showCellGuide(alignments, alignmentSet) {
            if (!alignments) {
                return;
            }    //for undefined
            console.log('draw cell guide');
            //Removes any pre-drawn cellGuides
            gGrid.selectAll('.mapCell-guide').remove();

            //Includes any alignment sets mapped to redundant nodes
            alignments = Array.isArray(alignments) ? alignments : [alignments];
            const allAlignments = alignments;
            for (let almt of alignments) {
                //Adds additional redundant alignment except itself
                const filtered = alignmentSet.filter(d => (d.namePair === almt.namePair) && (d === almt));
                allAlignments.concat(filtered);
            }

            //Draws guide rect to its mapped cell
            for (let almt of allAlignments) {
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

    grid(row, col, cellSize, drawsBg) {
        console.log('create background table lines.');
        const g = d3.create('svg:g');

        if (drawsBg) {
            g.append('rect')
                .classed('bg-rect', true)
                .attr('height', row * cellSize)
                .attr('width', col * cellSize);
        }

        const rowG = g.append('g').classed('row', true);
        for (let i = 0; i < row + 1; i++) {
            rowG.append('line')
                .attr('x1', 0)
                .attr('y1', i * cellSize)
                .attr('x2', col * cellSize)
                .attr('y2', i * cellSize);
        }

        const colG = g.append('g').classed('col', true);
        for (let i = 0; i < col + 1; i++) {
            colG.append('line')
                .attr('x1', i * cellSize)
                .attr('y1', 0)
                .attr('x2', i * cellSize)
                .attr('y2', row * cellSize);
        }

        return g.node();
    }
}