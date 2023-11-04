// global setting variable
const margin = {top: 30, right: 20, bottom: 30, left: 20};
const nodeHeight = 20,
      nodeWidth = 200,
      nodeIndent = 15,
      nodeMarkSize = 4.5;
const treeWidth = 300;

const triangle = trianglePoints(nodeMarkSize);
const verticalLink = d => `M ${d.source.x},${d.source.y} V ${d.target.y}`;


function hierarchy(data) {
    console.log('hierarcy() called.');
    //Creates hierarchy from data
    const root = d3.hierarchy(data);
    
    //Sort nodes alphabetically
    console.log('sort nodes in ascending order.');
    root.sort( (a,b) => d3.ascending(a.data.name, b.data.name) );

    //Sets the root position
    root.dx = 10;  //??: seems not necessary??
    root.dy = 0;
    root.descendants().forEach((d, i) => {
        d.id = i;   //assign id in breadth-first order
        d._children = d.children;   //for collapsing action
        d.shown = true;
    });
    console.log(root);
    return root;
}

/**
 * Builds and returns d3.tree object
 * by calculating the x,y positions of nodes in the tree.
 * @param {*} root d3.hierarchy root object
 * @param {*} align left or right
 * @return {d3.tree object} d3.tree object
 */
function tree(root, align) {
    console.log(`Computes tree layout. tree(root='${root.data.name}' align='${align}')`);
    let alignRight = (align == "right") ? true : false;

    //Creates tree root to return
    const treeRoot =  d3.tree().nodeSize([root.dx, root.dy])(root);
    //Sets the node positions
    var index = -1;
    treeRoot.eachBefore(function(d) {   //breadth-first pre-order traversal
        d.x = d.depth * nodeIndent * (alignRight ? -1 : 1); //gets indented
        d.y = ++index * nodeHeight;                         //lists down
    });
    // console.log(treeRoot);
    return treeRoot;
}

/**
 * Draws an indented tree from d3.hierarchy root
 * and returns a svg:g element.
 * @param {d3.hierarchy object} root d3.hierarchy root
 * @param {string} align left or right
 */
function treechart(root, align) {
    console.log(`treechart(root, align=${align})`);
    let alignRight = align === "right" ? true : false;

    //Creates a detached g to return
    const gTree = d3.create("svg:g")
        .classed('tree', true)
        .attr("transform", `translate(0,10)`); //??: why?
    const gLink = gTree.append("g")
        .classed('gLink', true)
        .attr('transform', `translate(0,${nodeHeight/2.5})`);
    const gNode = gTree.append("g")
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

    function update(source) {
        const nodes = root.descendants().reverse();  //for the z-order
        const links = linksToLastChild(root);

        // Coputes the new tree layout.
        tree(root, align);

        //TODO: auto resize the viewBox
        let lowest = 400;
        
        const t = gTree.transition().duration(100);
        
        // Updates the nodes...
        const node = gNode.selectAll("g")
            .data(nodes, d => d.id);

        // Enters any new nodes at the parent's previous position.
        const nodeEnter = node.enter().append("g").classed('node', true)
            .attr('id', d => `n${d.id}`)
            .classed('root', d => d==root)
            .classed('branch', d => d._children ? true : false) 
            .classed('expanded', d => d._children && !d.collapsed)
            .classed('leaf', d => d._children ? false : true)
            .attr("transform", d => `translate(${source.x0},${source.y0})`)
            .attr("opacity", 0);
        nodeEnter
            .on("click", d => {
                //for branch node, d.children: shown children, d._children: owned children.
                //for leaf node, d.children:undefined, d._children: undefined
                //gives null if d was the expanded branch to stop drawing,
                //or restores from _children if d was collapsed branch.
                d.children = d.children ? null : d._children;   
                if(d.children == null) {    //if it's collapsed branch node
                    console.log(`branch node '${d.data.name}' collapsed`);
                    d.collapsed = true;
                    //Sets shown=false to its actual descendants under d._children
                    d._children.forEach(d => d.descendants().forEach(dd => {dd.shown = false;}));
                    d.shown = true; //exclude self!
                    console.log(`${d.data.name}'s descendants gets shown=false`);
                    // console.log(d._children);
                } else if(d.children) {     //if it's expanded branch node
                    console.log(`branch node '${d.data.name}' expanded`);
                    d.collapsed = false;
                    d._children.forEach(d => d.descendants().forEach(dd => {dd.shown = true;}));
                    // console.log(d._children);
                }
                if(d._children) { //only for branch nodes with children
                    update(d); //update recursively
                }
            });
        // Appends nodemark, text, and select helper
        nodeEnter.append(d => {
                if(d._children) return d3.create("svg:polygon").attr('points', triangle).node();  //branch nodemark
                else            return d3.create("svg:circle").attr('r', 2).node();                //leaf nodemark
            }).classed('nodemark', true);
        nodeEnter.append("text")
            .attr("dy", "0.31em")
            .attr("x", alignRight ? -8 : 8)
            .attr("text-anchor", alignRight ? "end" : "start")
            .text(d => d.data.name);
        nodeEnter.append("rect").classed('node-select-helper', true)
            .attr('fill', 'transparent')
            .attr('width', nodeWidth)
            .attr('height', nodeHeight)
            .attr('x', alignRight ? -nodeWidth+8 : -8)
            .attr('y', -nodeHeight/2);
            // .lower();

        // Transition nodes to their new position.
        const nodeUpdate = node.merge(nodeEnter)
            .each((d,i,n) => {
                //Updates branch nodemark for expanded/collapsed
                if(d._children) {
                    // console.log('branch! expanded? '+d.expanded);
                    d3.select(n[i]).classed('expanded', !d.collapsed);
                }
            })
            .transition(t)
                .attr("transform", (d,i,n) => `translate(${d.x},${d.y})`)
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
                return verticalLink({source: o, target: o});
            });
        // Transition links to their new position.
        link.merge(linkEnter).transition(t)
            .attr("d", verticalLink);
        // Transition exiting nodes to the parent's new position.
        link.exit().transition(t).remove()
            .attr("d", d => {
                const o = {x: source.x, y: source.y};
                return verticalLink({source: o, target: o});
            });

        // Stash the old positions for transition.  <- for expand animation
        root.eachBefore(d => {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }

    //Initially updates the whole tree
    update(root);

    return gTree.node(); //returning the html element
}

/**
 * Generates a list of links that connects a parent to the last child of its last child.
 * from the hierarchy root.
 * This is to simplify the node link lines.
 * @param {*} root 
 */
function linksToLastChild (root) {
    const list = [];
    for (var node of root.descendants()) {
        if (node.children) {    //branch node with children
            list.push({source: node, target: getLastChild(node)});
        }
    }
    return list;

    // Recursively find the last of the last child
    function getLastChild (node) {
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
function trianglePoints(s) {
    // d3.create('polygon');
    return `-${s-1},-${s} -${s-1},${s} ${s-1},0 -${s-1},-${s}`;
}