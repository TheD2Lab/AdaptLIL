
/**
 * Builds alignment list with "id, entity1, and entity2" pairs including redundant matches
 * @param {*} sourceAlmts 
 * @returns {*} newAlmts
 */
function buildNewAlignments(sourceAlmts, ont1root, ont2root) {
    console.log('buildNewAlignments()');
    let newAlmts = [];
    let id = 0;
    sourceAlmts.forEach(almt => {
        e1matches = ont1root.descendants().filter(d => d.data.name === almt.entity1);
        e2matches = ont2root.descendants().filter(d => d.data.name === almt.entity2);
        namePair = {entity1: almt.entity1, entity2: almt.entity2};
        for (let e1 of e1matches) {
            for (let e2 of e2matches) {
                newAlmts.push({id:id++, namePair:namePair, e1:e1, e2:e2});
            }
        }
    });
    console.log(newAlmts);
    updateMappingPos(newAlmts);
    console.log(newAlmts);
    return newAlmts;
}

/**
 * Gets all mappings of its descendants
 * @param {*} d tree node element
 */
function getAllDescendantMappings(d) {
    mappings = d.mappings ? d.mappings : [];
    //Includes those mappings of subnodes
    if (d._children) {
      for (let child of d._children) {
        for (let dsc of child.descendants()) {
          if (dsc.mappings) {
            mappings = mappings.concat(dsc.mappings);
          }
        }
      }
    }
    return mappings;
}

/**
 * Updates the mapped positions of alignments
 * @param {*} alignments 
 */
function updateMappingPos(alignments) {
    console.log('updateMappingPos()');
    alignments.forEach(a => {
        //Updates the positions
        if (a.e1.shown) {
            a.e1pos = {x: a.e1.x, y: a.e1.y};
        } else {
            const nearestShownAnc = a.e1.ancestors().filter(d => d.shown)[0];
            a.e1pos = {x: nearestShownAnc.x, y: nearestShownAnc.y};
        }
        if (a.e2.shown) {
            a.e2pos = {x: a.e2.x, y: a.e2.y};
        } else {
            const nearestShownAnc = a.e2.ancestors().filter(d => d.shown)[0];
            a.e2pos = {x: nearestShownAnc.x, y: nearestShownAnc.y};
        }

        //Marks mapToHidden if one of mappedEntity is shown false
        a.mapToHidden = !(a.e1.shown && a.e2.shown);
    });
    
    //Marks overlapped top nodes for triangle
    alignments.forEach(a => {
        const overlapped = alignments.filter(other => (a.e1pos.y == other.e1pos.y) && (a.e2pos.y == other.e2pos.y));
        a.overlappedTop = (overlapped.length > 1 && !a.mapToHidden);
    });
}

/**
 * Give 'highlight' class to the DOM elements of the list of alignments
 * @param {*} alignments one alignment or an array of alignments
 * @param {*} g svg:g of baseline or matrix
 * @param {*} alignmentSet base_alignment or mtrx_alignment
 */
function highlightAlignment(alignments, g, alignmentSet) {
    if (!alignments) { return; }    //for undefined
    console.log('highlightAlignment()');
    //Mutes all mapping and nodes in the group
    g.selectAll('.node').classed('muted', true);
    g.selectAll('.mapping').classed('muted', true);

    //Includes any alignment sets mapped to redundant nodes
    alignments = Array.isArray(alignments) ? alignments : [alignments];
    const allAlignments = alignments;
    for(let almt of alignments) {
        //Adds additional redundant alignment except itself
        filtered = alignmentSet.filter(d => (d.namePair === almt.namePair) && (d === almt));
        allAlignments.concat(filtered);
    }

    console.log(allAlignments);
    //Highlights mappings and their class nodes
    for (let almt of allAlignments.reverse()) {
        // console.log(`highlight: gMap #a${almt.id}, gTree1 #n${almt.e1.id} '${almt.e1.data.name}', gTree2 #n${almt.e2.id} '${almt.e2.data.name}'`);

        //alignment
        g.select("#gMap").select('#a'+almt.id)
            .classed('muted', false)
            .classed('highlight', true).raise();
        //tree nodes
        g.select("#gTree1").select('#n'+almt.e1.id)
            .classed('muted', false)
            .classed('highlight', true);
        g.select("#gTree2").select('#n'+almt.e2.id)
            .classed('muted', false)
            .classed('highlight', true);
    }
    //Always place direct mappings on top.
    g.selectAll('.mapping').filter(d => d.overlappedTop).raise();
    allAlignments.forEach(almt => {
        if(almt.overlappedTop)
            g.select('#gMap').select('#a'+almt.id).raise();
    });
}

function unhighlightAll(g) {
    g.selectAll("*")
        .classed('highlight', false)
        .classed('muted', false);
    //Always place direct mappings on top.
    g.selectAll('.map-to-hidden').lower();
}
