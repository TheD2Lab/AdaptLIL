var taskDatasets = {
  conference: {
    domain: "conference",
    tasks: [
      {
        "qtype": "identifying", // correct mapping
        "question": "How many mappings are shown in the visualization in total?",
        "atype": "number",
        "options": ["6", "9", "10", "11"],
        "answer": "10"
      },
      {
        "qtype": "identifying", // incorrect mapping - multiple inheritance
        "question": "How many classes is \"Author\" (in the left ontology) mapped to?",
        "atype": "number",
        "options":["0", "1", "2", "3"],
        "answer": "1"
      },
      {
        "qtype": "identifying", // incorrect mapping
        "question": "What is \"SlideSet\" (in the left ontology) mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "Document"
      },
      {
        "qtype": "validation", // correct mapping
        "question": "Is there a mapping between \"AcademicEvent\" (in the left ontology) and \"Scientific_Event\" (in the right ontology)?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "validation", // correct mapping
        "question": "Is \"AcademiaOrganization\" (in the left ontology) correctly mapped?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "identifying", // missing mapping
        "question": "Can \"Person\" (in the left ontology) be mapped to another class (in the right ontology)?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "creation", // missing mapping
        "question": "Which class could \"Attendee\" (in the left ontology) be mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "Conference_Participant"
      },
      {
        "qtype": "identifying", // correct mapping
          "question": "Which class could \"ConferenceDinner\" (in the left ontology) mapped to (in the right ontology)?",
          "atype": "class",
          "answer": "Conference_Banquet"
        },
      {
        "qtype": "validation", // incorrect mapping
        "question": "\"SecurityTopic\" (in the left ontology) is mapped to \"Research_Topic\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "validation", // correct mapping
        "question": "\"Place\" (in the left ontology) is mapped to \"Location\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "identifying", // incorrect mapping
        "question": "\"RejectedPaper\" (in the left ontology) is mapped to \"Assigned_Paper\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "validation", // incorrect mapping
        "question": "\"IndustryOrganization\" (in the left ontology) is mapped to \"Organisation\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "creation", // missing mapping
        "question": "Which class could \"AcceptedPaper\" (in the left ontology) be mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "Accepted_Paper"
      },
      {
        "qtype": "identifying", // missing mapping
        "question": "Can \"Workshop\" (in the left ontology) be mapped to another class (in the right ontology)?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "creation", // missing mapping
        "question": "Is there any other mapping(s) that should be created between the ontologies but is currently absent from the visualization? List as many as you can (ex. \"AcademicEvent=Scientific_Event, SlideSet=Document,...\").",
        "atype": "pairs",
        "answer": [ // multiple (8)
          "Organization=Organisation",
          "Author=Paper_Author",
          "Topic=Research_Topic",
          "Document=Document",
          "RejectedPaper=Rejected_Paper",
          "Workshop=Workshop",
          "Person=Person",
          "Paper=Paper",
        ] 
      },
    ]
  },
  anatomy:  {
    domain: "anatomy",
    tasks: [
      {
        "qtype": "identifying", // correct mapping
        "question": "How many mappings are shown in the visualization in total?",
        "atype": "number",
        "options": ["4", "8", "10", "12"],
        "answer": "10"
      },
      {
        "qtype": "identifying", // correct mapping
        "question": "How many classes is \"Skin\" (in the left ontology) mapped to?",
        "atype": "number",
        "options": ["0", "1", "2", "4"],
        "answer": "1"
      },
      {
        "qtype": "identifying", // correct mapping
        "question": "What is \"Viscera\" (in the left ontology) mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "visceral organ system"
      },
      {
        "qtype": "identifying", // missing mapping
        "question": "Is there a mapping between \"Blood\" (in the left ontology) and \"blood\" (in the right ontology)?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "validation", // incorrect mapping
        "question": "Is \"Cartilage\" (in the left ontology) correctly mapped?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "identifying", // missing mapping
        "question": "Can \"Joint\" (in the left ontology) be mapped to another class (in the right ontology)?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "creation", // missing mapping
        "question": "Which class could \"Heart\" (in the left ontology) be mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "heart"
      },
      {
        "qtype": "identifying", // correct mapping
        "question": "What is \"Skull\" (in the left ontology) mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "cranium"
      },
      {
        "qtype": "validation", // incorrect mapping
        "question": "\"Urinary_System_Part\" (in the left ontology) is mapped to \"muscle\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "validation", // incorrect mapping
        "question": "\"Cheek\" (in the left ontology) is mapped to \"cuticle\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "identifying", // correct mapping
        "question": "\"Skin\" (in the left ontology) is mapped to \"skin\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "validation", // incorrect mapping
        "question": "\"Mucus\" (in the left ontology) is mapped to \"nasal mucus\" (in the right ontology). Is this correct?",
        "atype": "y/n",
        "answer": "No"
      },
      {
        "qtype": "creation", // missing mapping
        "question": "Which class could \"Lip\" (in the left ontology) be mapped to (in the right ontology)?",
        "atype": "class",
        "answer": "Lip"
      },
      {
        "qtype": "identifying", // missing mapping
        "question": "Can \"Arm\" (in the left ontology) be mapped to another class (in the right ontology)?",
        "atype": "y/n",
        "answer": "Yes"
      },
      {
        "qtype": "creation", // missing mapping
        "question": "Is there any other mapping(s) that should be created between the ontologies but is currently absent from the visualization? List as many as you can (ex. \"Skin=skin, Skull=cranium,...\").",
        "atype": "pairs",
        "answer": [ // multiple (8)
          "Nasal_Mucus=nasalmucus",
          "Muscle=muscle",
          "Cartilage=cartilage",
          "Cheek=cheek",
          "Body_Fluid_or_Substance=bodyfluid/substance",
          "Joint=joint",
          "Blood=blood",
          "Arm=arm",
        ]
      },
    ]
  }
};