class LinkIndentedList {

    constructor() {
        this.nodeHeight = 20;
        this.nodeWidth = 200;
        this.nodeIndent = 15;
        this.nodeMarkSize = 4.5;
        this.hierarchies = [];
        this.triangle = this.trianglePoints(this.nodeMarkSize);
        this.adaptations = new VisualizationAdaptations(this);
        this.verticalLink = d => `M ${d.source.x},${d.source.y} V ${d.target.y}`;
    }

    createHierarchy(data) {
        console.log('creating hierarchy for node axis.');

        //Creates hierarchy from data
        const root = d3.hierarchy(data);
        const hierarchiesByIndex = {};

        //Sort nodes alphabetically
        root.sort((a, b) => d3.ascending(a.data.name, b.data.name));

        //Sets the root position
        root.dx = 10;  //??: seems not necessary??
        root.dy = 0;
        root.descendants().forEach((d, i) => {
            d.id = i;   //assign id in breadth-first order
            d._children = d.children;   //for collapsing action
            d.shown = true;
            hierarchiesByIndex[i] = d;
        });
        this.hierarchies.push(hierarchiesByIndex);
        return root;
    }

    /**
     * Builds and returns d3.tree object
     * by calculating the x,y positions of nodes in the tree.
     * @param {*} root d3.hierarchy root object
     * @param {*} align left or right
     * @return {d3.tree object} d3.tree object
     */
    constructTree(root, align) {
        const _this = this;
        console.log(`Computes tree layout. tree(root='${root.data.name}' align='${align}')`);

        const alignRight = (align == "right") ? true : false;

        //Creates tree root to return
        const treeRoot = d3.tree().nodeSize([root.dx, root.dy])(root);
        //Sets the node positions
        let index = -1;
        treeRoot.eachBefore(function (d) {   //breadth-first pre-order traversal
            d.x = d.depth * _this.nodeIndent * (alignRight ? -1 : 1); //gets indented
            d.y = ++index * _this.nodeHeight;                         //lists down
        });

        return treeRoot;
    }


    expandAxisTick(gTree, source, d, align) {
        //for branch node, d.children: shown children, d._children: owned children.
        //for leaf node, d.children:undefined, d._children: undefined
        //gives null if d was the expanded branch to stop drawing,
        //or restores from _children if d was collapsed branch.
        d.children = d.children ? null : d._children;
        if (d.children == null) {    //if it's collapsed branch node
            console.log(`branch node '${d.data.name}' collapsed`);
            d.collapsed = true;

            if (d._children != undefined) {

                //Sets shown=false to its actual descendants under d._children
                d._children.forEach(d => d.descendants().forEach(dd => {
                    dd.shown = false;
                }));
            }
            d.shown = true; //exclude self!
            console.log(`${d.data.name}'s descendants gets shown=false`);
        } else if (d.children) {     //if it's expanded branch node
            console.log(`branch node '${d.data.name}' expanded`);
            d.collapsed = false;
            d._children.forEach(d => d.descendants().forEach(dd => {
                dd.shown = true;
            }));
        }
        if (d._children) { //only for branch nodes with children
            this.update(gTree, d, source, align); //update recursively
        }
    }

    /**
     * Draws an indented tree from d3.hierarchy root
     * and returns a svg:g element.
     * @param {d3.hierarchy object} root d3.hierarchy root
     * @param {string} align left or right
     */
    treechart(root, align) {
        const _this = this;
        console.log(`treechart(root, align=${align})`);
        let alignRight = align === "right" ? true : false;

        //Creates a detached g to return
        const gTree = d3.create("svg:g")
            .classed('tree', true)
            .attr("transform", `translate(0,10)`); //??: why?

        gTree.append("g")
            .classed('gLink', true)
            .attr('transform', `translate(0,${this.nodeHeight / 2.5})`);

        gTree.append("g")
            .classed('gNode', true);

        //Sets initial state as tree collapsed to the top branches under root
        console.log('Sets initial state as collapsed to top branches');
        root.descendants().forEach(d => {
            //Collapses all branches
            if (d.depth > 0 && d.children) {
                d.children = null;
                d.collapsed = true;
            }
            //Not show any nodes deeper than depth 1
            if (d.depth > 1) {
                d.shown = false;
            }
        });

        //Initially updates the whole tree
        this.update(gTree, root, root, align);

        return gTree.node(); //returning the html element
    }

    update(gTree, source, root, align) {
        const _this = this;
        const nodes = root.descendants().reverse();  //for the z-order

        const links = _this.linksToLastChild(root);
        const alignRight = (align == "right") ? true : false;

        // Computes the new tree layout.
        _this.constructTree(root, align);

        const t = gTree.transition().duration(100);

        //Get link
        const gLink = gTree.selectAll("g.gLink")

        // Updates the nodes...
        const gNode = gTree.selectAll("g.gNode");
        const node = gNode.selectAll("g")
            .data(nodes, d => d.id);

        // Enters any new nodes at the parent's previous position.
        const nodeEnter = node.enter().append("g").classed('node', true)
            .attr('id', d => `n${d.id}`)
            .classed('root', d => d == root)
            .classed('branch', d => d._children ? true : false)
            .classed('expanded', d => d._children && !d.collapsed)
            .classed('leaf', d => d._children ? false : true)
            .attr("transform", d => `translate(${source.x0},${source.y0})`)
            .attr("opacity", 0);

        nodeEnter
            .on("click", d => _this.expandAxisTick(gTree, root, d, align));

        // Appends nodemark, text, and select helper
        nodeEnter.append(d => {
            if (d._children) return d3.create("svg:polygon").attr('points', _this.triangle).node();  //branch nodemark
            else return d3.create("svg:circle").attr('r', 2).node();                //leaf nodemark
        }).classed('nodemark', true);

        const text_start = nodeEnter
            .append('text')
            .attr("dy", "0.31em")
            .attr("x", alignRight ? -8 : 8)
            .attr("text-anchor", alignRight ? "end" : "start")
            .text(d => d.data.name)

        nodeEnter.append("rect").classed('node-select-helper', true)
            .attr('fill', 'transparent')
            .attr('width', _this.nodeWidth)
            .attr('height', _this.nodeHeight)
            .attr('x', alignRight ? -_this.nodeWidth + 8 : -8)
            .attr('y', -_this.nodeHeight / 2);


        // Transition nodes to their new position.
        const nodeUpdate = node.merge(nodeEnter)
            .each((d, i, n) => {
                //Updates branch nodemark for expanded/collapsed
                if (d._children) {
                    // console.log('branch! expanded? '+d.expanded);
                    d3.select(n[i]).classed('expanded', !d.collapsed);
                }
            })
            .transition(t)
            .attr("transform", (d, i, n) => `translate(${d.x},${d.y})`)
            .attr("opacity", 1);

        // Transition exiting nodes to the parent's new position.
        const nodeExit = node.exit().transition(t).remove()
            .attr("transform", d => `translate(${source.x},${source.y})`)
            .attr("opacity", 0);

        // Update the deapth guide lines…
        const link = gLink.selectAll("path")
            .data(links, d => d.target.id);

        // Enter any new links at the parent's previous position.
        const linkEnter = link.enter().append("path")
            .classed('nodelink', true)
            .attr("d", d => {
                const o = {x: source.x0, y: source.y0};
                return _this.verticalLink({source: o, target: o});
            });

        // Transition links to their new position.
        link.merge(linkEnter).transition(t)
            .attr("d", _this.verticalLink);

        // Transition exiting nodes to the parent's new position.
        link.exit().transition(t).remove()
            .attr("d", d => {
                const o = {x: source.x, y: source.y};
                return _this.verticalLink({source: o, target: o});
            });

        // Stash the old positions for transition.  <- for expand animation
        root.eachBefore(d => {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }

    /**
     * Generates a list of links that connects a parent to the last child of its last child.
     * from the hierarchy root.
     * This is to simplify the node link lines.
     * @param {*} root
     */

    linksToLastChild(root) {
        const list = [];
        for (const node of root.descendants()) {
            if (node.children) {    //branch node with children
                list.push({source: node, target: getLastChild(node)});
            }
        }
        return list;

        // Recursively find the last of the last child
        function getLastChild(node) {
            if (node.children) {
                return getLastChild(node.children[node.children.length - 1]);
            } else {
                return node;
            }
        }
    }


    /**
     * Calculates triangle points
     * @param {number} s radius size
     */
    trianglePoints(s) {
        // d3.create('polygon');
        return `-${s - 1},-${s} -${s - 1},${s} ${s - 1},0 -${s - 1},-${s}`;
    }


}