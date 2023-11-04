mapping_edas_ekaw = {
	"onto1": "http://edas",
	"onto2": "http://ekaw",
	"alignments": [
		//correct mappings
		{
			"entity1": "ConferenceDinner",
			"entity2": "Conference_Banquet"
		},
		{
			"entity1": "AcademicEvent",
			"entity2": "Scientific_Event"
		},
		{
			"entity1": "Place",
			"entity2": "Location"
		},
		{
			"entity1": "AcademiaOrganization",
			"entity2": "Academic_Institution"
		},
		{
			"entity1": "ConferenceEvent",
			"entity2": "Event"
		},
		//incorrect mappings
		{
			"entity1": "IndustryOrganization", // "Organization"
			"entity2": "Organisation"
		},
		{
			"entity1": "Author",
			"entity2": "Tutorial_Chair" // "Paper_Author"
		},
		{
			"entity1": "SecurityTopic", // "Topic"
			"entity2": "Research_Topic"
		},
		{
			"entity1": "SlideSet", // "Document"
			"entity2": "Document"
		},
		{
			"entity1": "RejectedPaper",
			"entity2": "Assigned_Paper" // "Rejected_Paper"
		},
		//missing mappings
		// {
		// 	"entity1": "AcceptedPaper",
		// 	"entity2": "Accepted_Paper"
        // },
		// {
		// 	"entity1": "Workshop",
		// 	"entity2": "Workshop"
		// },
		// {
		// 	"entity1": "Person",
		// 	"entity2": "Person"
		// },
		// {
		// 	"entity1": "Paper",
		// 	"entity2": "Paper"
		// },
		// {
		// 	"entity1": "Attendee",
		// 	"entity2": "Conference_Participant"
		// },
	]
};