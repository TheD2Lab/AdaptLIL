//Processed data to use
let base_ont1root;
let base_ont2root;
let base_alignments;



class BaselineMap {

    constructor(linkIndentedList) {
        this.linkIndentedList = linkIndentedList;
        //To make highlight stay on mouse click on mapping line.
        this.maplineClicked = false;
        this.maplines = undefined; // mapline is the line connecting the nodes.
        this.resetOpacity = false;
        this.maplinesClicked = {}
    }
    /**
     * Creates baselineMapping and returns svg:g
     * @param {*} ont1root
     * @param {*} ont2root
     * @param {*} alignments
     */
    baselineMapping(ont1root, ont2root, alignments) {
        console.log(`baselineMappingVis(ont1root, ont2root, alignments)`);

        //Creates a detached g to return
        const g = d3.create("svg:g");
    }

    setDataset(dataset) {
        //Builds proper structures for ontologies and alignments
        const ont1TreeRoot = this.linkIndentedList.constructTree(this.linkIndentedList.createHierarchy(dataset.ont1.root), 'right');
        const ont2TreeRoot = this.linkIndentedList.constructTree(this.linkIndentedList.createHierarchy(dataset.ont2.root), 'left');
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



    /**
     * Draws baseline mapping svg
     */
    drawBaselineSvg() {
        console.log("drawBaselineSvg()");
        const _this = this;
        const ontGap = 200;

        _this.svg = d3.select("#baseline-svg")
            .attr('height', 2100)   //TODO: calculate auto
            .attr('width', 1050);   //TODO: adjust to the same width of svg div
        const svgWidth = + _this.svg.attr('width');
        // //auto adjust the svg height
        // let newHeight = nodeHeight * Math.max(ont1TreeRoot.count(), ont2TreeRoot.count());
        // svg.attr('height', newHeight);
        const g = _this.svg.append('g')
            .attr('transform', `translate(${svgWidth / 2},20)`);
        const gTree1 = g.append(() => this.linkIndentedList.treechart(base_ont1root, "right"))
            .attr('id', 'gTree1')
            .classed('right-aligned', true)
            .attr('transform', `translate(${-ontGap / 2},0)`);

        const gTree2 = g.append(() => this.linkIndentedList.treechart(base_ont2root, "left"))
            .attr('id', 'gTree2')
            .attr('transform', `translate(${ontGap / 2},0)`);
        const gMap = g.append('g')
            .attr('id', 'gMap')
            .attr('transform', `translate(${-ontGap / 2},0)`); //to center

        console.log('draw baseline mapping');

        function update() {
            console.log('update mapLines');

            updateMappingPos(base_alignments);


            _this.maplines = gMap.selectAll('g')
                .data(base_alignments, d => d.id);

            _this.renderMapLines(_this.maplines, g, gMap);


            //Highlights alignments for mouse events on tree nodes
            g.selectAll('.node')
                .filter(d => d.mappingsOfDescendants)
                .on('mouseover', function(d) {
                    const mappings = d.collapsed ? d.mappingsOfDescendants : d.mappings;


                    if (_this.linkIndentedList.adaptations.deemphasisEnabled) {
                        deemphasize(mappings, g, base_alignments, _this.linkIndentedList.adaptations.deemphasisAdaptation, _this.maplinesClicked);
                        _this.resetOpacity = true;
                    }

                    if (_this.linkIndentedList.adaptations.highlightingEnabled) {
                        if (!_this.maplineClicked) {
                            if (mappings) {

                                highlightAlignment(mappings, g, base_alignments, _this.linkIndentedList.adaptations.highlightAdaptation);
                            } else {
                                //Is gtree1 or gtree 2?
                                //g in this case needs to be the closest tree
                                highlightText(d3.select(this.parentElement.parentElement), d, _this.linkIndentedList.adaptations.highlightAdaptation);
                            }
                        }
                    }

                })
                .on('mouseout', () => {

                    //Deemphasis adaptation
                    if (_this.linkIndentedList.adaptations.deemphasisEnabled) {
                            restoreOpacity(g, base_alignments, _this.maplinesClicked);
                            _this.resetOpacity = false;

                    }

                    //Highlight Adaptation
                    if (_this.linkIndentedList.adaptations.highlightingEnabled) {
                        if (!_this.maplineClicked) {
                            unhighlightAll(g);
                        }
                    }

                });

        }

        update();
        //Redraws mapping lines
        gTree1.on('click', () => update());
        gTree2.on('click', () => update());


        //Turns off the highlight when clicked on other part in svg
        document.getElementById('baseline-svg')
            .addEventListener('click', (e) => {
                const isMapLineTargeted = d3.select(e.target.parentNode).classed('mapLine') || d3.select(e.target.parentNode).classed('node');
                if (_this.maplineClicked && !isMapLineTargeted) {
                    if (_this.linkIndentedList.adaptations.highlightingEnabled) {
                        unhighlightAll(g);
                    }

                    _this.maplineClicked = false;
                    _this.maplinesClicked = {};
                }
            });


    }

    /**
     * Calculates the svg:path for a baseline mapping line
     * @param {Object} almt an alignment mapping
     * @param {number} i the index of the alignment
     */
    calcMapLinePath(almt, i) {
        if (!almt) {
            return '';
        }
        // console.log(`calcMapLinePath() id:${almt.id} e1pos:${almt.e1pos.x},${almt.e1pos.y} e2pos:${almt.e2pos.x},${almt.e2pos.y}`);

        const ontGap = 200;
        const dn = 6; //distance from the nodemark
        const x1 = almt.e1pos.x + dn,
            y1 = almt.e1pos.y,
            x2 = almt.e2pos.x + ontGap - dn,
            y2 = almt.e2pos.y;
        const c = 10,   //curve value
            gm = 20, //margin from ontGap
            hgap = ((ontGap - gm * 2) / base_alignments.length).toFixed(0);
        const hx = hgap * i + gm;
        const vy = y2 > y1 ? y2 - c : y2 + c;
        const cy = y2 > y1 ? c : -c;
        if (y1 === y2) {
            return `M${x1},${y1} H${x2}`;
        } //return straight line
        return `M${x1},${y1} H${hx} s${c},0,${c},${cy} V${vy} s0,${cy},${c},${cy} H${x2}`;
    }

    /**
     * Render logic for the map lines that connect two entities/nodes
     * @param maplines
     * @param svg
     * @param svgMap
     */
    renderMapLines(maplines, svg, svgMap) {
        const _this = this;
        const t = svgMap.transition().duration(100);

        const maplineEnter = maplines.enter()
            .append('g')
            .attr('id', d => `a${d.id}`)
            .classed('mapping', true).classed('mapLine', true);

        maplineEnter
            .on('click', almt => {
                _this.maplineClicked = true;
                
                _this.maplinesClicked[almt.id] = almt;
                //we're going to need nodesOfClickedMapLines id by node id.
                //Highlight Adaptation
                if (_this.linkIndentedList.adaptations.highlightAdaptation.state) {
                    highlightAlignment(almt, svg, base_alignments, _this.linkIndentedList.adaptations.highlightAdaptation);
                }

                if (_this.linkIndentedList.adaptations.deemphasisAdaptation.state) {
                    deemphasize(almt, svg, base_alignments, _this.linkIndentedList.adaptations.deemphasisAdaptation, _this.maplinesClicked);
                }

            })
            .on('mouseover', almt => {

                //deemphasis adaptation
                if (_this.linkIndentedList.adaptations.deemphasisAdaptation.state) {
                    //Need to pass in currenty clicked
                    deemphasize(almt, svg, base_alignments, _this.linkIndentedList.adaptations.deemphasisAdaptation, _this.maplinesClicked);
                    _this.resetOpacity = true;
                }

                //highlight adaptation
                if (_this.linkIndentedList.adaptations.highlightAdaptation.state) {
                    if (!_this.maplineClicked) {
                        highlightAlignment(almt, svg, base_alignments, _this.linkIndentedList.adaptations.highlightAdaptation);
                    }
                }

            })
            .on('mouseout', almt => {

                //Deemphasis adaptation
                if (_this.linkIndentedList.adaptations.deemphasisEnabled) {
                    if (_this.resetOpacity) {
                        restoreOpacity(svg, base_alignments, _this.maplinesClicked);
                        _this.resetOpacity = false;
                    }
                }

                //Highlight Adaptation
                if (_this.linkIndentedList.adaptations.highlightingEnabled) {
                    if (!_this.maplineClicked)
                        unhighlightAll(svg);
                }

            });

        maplineEnter.append('path')   //foreground path
            .attr('d', (d, i) => _this.calcMapLinePath(d, i))
            .attr('fill', 'none')
            .attr('class', 'mapLine-fg')
            .clone(true).lower() //background path
            .attr('class', 'mapLine-bg')
            .clone(true).lower() //path select helper
            .attr('class', 'mapLine-select-helper')

        const maplineUpdate = _this.maplines.merge(maplineEnter)
            .classed('map-to-hidden', d => d.mapToHidden)
            .transition(t)
            .each((d, i, n) => {
                d3.select(n[i]).selectAll('path')
                    .attr('d', () => _this.calcMapLinePath(d, i));
            });
        //Always place direct mappings on top.
        svgMap.selectAll('.map-to-hidden').lower();
        _this.refreshMapLineColors();
        const maplineExit = _this.maplines.exit().transition(t).remove();
    }


    /**
     * If the colorScheme adaptation is toggled on then the mapLine color schemes will be applied.
     */
    refreshMapLineColors() {
        const _this = this;

        //TODO
        //Add multi node levels.
        if (_this.linkIndentedList.adaptations.colorSchemeEnabled) {
            d3.selectAll('*:not(.map-to-hidden)>.mapLine-fg').style('stroke',
                _this.linkIndentedList.adaptations.colorSchemeAdaptation.styleConfig.map_to_not_hidden_color);
            d3.selectAll('.map-to-hidden>.mapLine-fg').style('stroke',
                _this.linkIndentedList.adaptations.colorSchemeAdaptation.styleConfig.map_to_hidden_color)

        } else {
            d3.selectAll('.mapLine-fg').style('stroke', '#0077ff') //default color
        }
    }

    getEntityTrees() {
        const _this = this;
        const json = {};

    }

    getMapLineJson() {
        const _this = this;
        const json = {}
        _this.maplines.enter().each(function(d) {
            json[d.id] = {
                e1: {
                    pos: d.e1pos,
                    depth: d.e1.depth,
                    name: d.e1.data.name,
                    parent: d.e1.parent.name
                },
                e2: {
                    pos: d.e2pos,
                    depth : d.e2.depth,
                    name: d.e2.data.name,
                    parent: d.e2.parent.name
                }
            }
        });
        console.log(json)
        return json;
    }

}


