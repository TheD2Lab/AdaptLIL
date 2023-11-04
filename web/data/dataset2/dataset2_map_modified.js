var mapping_human_mouse = {
    "onto2": "http://mouse.owl",
    "onto1": "http://human.owl",
    "alignments": [
        //correct mappings
        {
            "entity2": "eyelash",
            "entity1": "Eyelash"
        },
        {
            "entity2": "cranium",
            "entity1": "Skull"
        },
        {
            "entity2": "leg",
            "entity1": "Leg"
        },
        {
            "entity2": "skin",
            "entity1": "Skin"
        },
        {
            "entity2": "visceral organ system",
            "entity1": "Viscera"
        },
        //incorrect mappings
        {
            "entity2": "nasal mucus",
            "entity1": "Mucus" //Nasal_Mucus
        },
        {
            "entity2": "muscle",
            "entity1": "Urinary_System_Part" //Muscle
        },
        {
            "entity2": "tendon", //cartilage
            "entity1": "Cartilage"
        },
        {
            "entity2": "cuticle", //cheek
            "entity1": "Cheek"
        },
        {
            "entity2": "sweat", //body fluid/substance
            "entity1": "Body_Fluid_or_Substance"
        },
        //missing mappings
        // {
        //     "entity2": "joint",
        //     "entity1": "Joint"
        // },
        // {
        //     "entity2": "blood",
        //     "entity1": "Blood"
        // },
        // {
        //     "entity2": "lip",
        //     "entity1": "Lip"
        // },
        // {
        //     "entity2": "arm",
        //     "entity1": "Arm"
        // },
        // {
        //     "entity2": "heart",
        //     "entity1": "Heart"
        // },
    ]
};