var edas;
var ekaw;
var mapping_edas_ekaw;

edas = {
    "name": "edas",
    "baseNS": "http://edas",
    "classCount": 103,
    "root": {
      "name": "Thing",
      "children": [
        {
          "name": "Person",
          "children": [
            {
              "name": "ConferenceChair",
              "children": []
            },
            {
              "name": "TPCMember",
              "children": []
            },
            {
              "name": "Attendee",
              "children": []
            },
            {
              "name": "SessionChair",
              "children": []
            }
          ]
        },
        {
          "name": "SingleLevelConference",
          "children": []
        },
        {
          "name": "Topic",
          "children": [
            {
              "name": "MicroelectronicsTopic",
              "children": []
            },
            {
              "name": "MultimediaTopic",
              "children": []
            },
            {
              "name": "SignalProcessingTopic",
              "children": []
            },
            {
              "name": "CommunicationsTopic",
              "children": []
            },
            {
              "name": "OperatingTopicsystems",
              "children": []
            },
            {
              "name": "CryptographyTopic",
              "children": []
            },
            {
              "name": "ParallelAndDistributedComputingTopic",
              "children": []
            },
            {
              "name": "ComputerArchitectureTopic",
              "children": []
            },
            {
              "name": "SatelliteAndSpaceCommunicationsTopic",
              "children": []
            },
            {
              "name": "MedicineTopic",
              "children": []
            },
            {
              "name": "TestOnlyTopic",
              "children": []
            },
            {
              "name": "CADTopic",
              "children": []
            },
            {
              "name": "SecurityTopic",
              "children": []
            },
            {
              "name": "RadioCommunicationsTopic",
              "children": []
            },
            {
              "name": "CommunicationTheoryTopic",
              "children": []
            },
            {
              "name": "AntennasTopic",
              "children": []
            },
            {
              "name": "WirelessCommunicationsTopic",
              "children": []
            },
            {
              "name": "ComputerNetworksTopic",
              "children": [
                {
                  "name": "ComputerNetworksSecurityTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksMeasurementsTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksAapplicationsTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksOpticalTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksSensorTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksSwitchingTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksEnterpriseTopic",
                  "children": []
                },
                {
                  "name": "ComputerNetworksManagementTopic",
                  "children": []
                }
              ]
            },
            {
              "name": "PowerlineTransmissionTopic",
              "children": []
            },
            {
              "name": "MobileComputingTopic",
              "children": []
            },
            {
              "name": "PerformanceTopic",
              "children": []
            }
          ]
        },
        {
          "name": "Country",
          "children": []
        },
        {
          "name": "ReviewForm",
          "children": []
        },
        {
          "name": "Author",
          "children": [
            {
              "name": "Presenter",
              "children": []
            }
          ]
        },
        {
          "name": "Reviewer",
          "children": []
        },
        {
          "name": "PersonalHistory",
          "children": [
            {
              "name": "PersonalPublicationHistory",
              "children": []
            },
            {
              "name": "PersonalReviewHistory",
              "children": []
            }
          ]
        },
        {
          "name": "ReviewQuestion",
          "children": [
            {
              "name": "TextualReviewQuestion",
              "children": []
            },
            {
              "name": "NumericalReviewQuestion",
              "children": []
            }
          ]
        },
        {
          "name": "ContactInformation",
          "children": []
        },
        {
          "name": "TwoLevelConference",
          "children": []
        },
        {
          "name": "ConferenceSession",
          "children": []
        },
        {
          "name": "Place",
          "children": [
            {
              "name": "AccommodationPlace",
              "children": []
            },
            {
              "name": "MeetingRoomPlace",
              "children": []
            },
            {
              "name": "DiningPlace",
              "children": []
            },
            {
              "name": "ConferenceVenuePlace",
              "children": []
            }
          ]
        },
        {
          "name": "Sponsorship",
          "children": [
            {
              "name": "TravelGrant",
              "children": []
            }
          ]
        },
        {
          "name": "Organization",
          "children": [
            {
              "name": "AcademiaOrganization",
              "children": []
            },
            {
              "name": "IndustryOrganization",
              "children": []
            },
            {
              "name": "NGO",
              "children": []
            },
            {
              "name": "GovernmentOrganization",
              "children": []
            }
          ]
        },
        {
          "name": "Call",
          "children": [
            {
              "name": "CallForManuscripts",
              "children": []
            },
            {
              "name": "CallForReviews",
              "children": []
            },
            {
              "name": "CallForPapers",
              "children": []
            }
          ]
        },
        {
          "name": "Document",
          "children": [
            {
              "name": "SlideSet",
              "children": []
            },
            {
              "name": "MealMenu",
              "children": []
            },
            {
              "name": "Programme",
              "children": []
            },
            {
              "name": "Review",
              "children": []
            },
            {
              "name": "Paper",
              "children": [
                {
                  "name": "ActivePaper",
                  "children": [
                    {
                      "name": "RatedPapers",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "RejectedPaper",
                  "children": []
                },
                {
                  "name": "PendingPaper",
                  "children": []
                },
                {
                  "name": "PublishedPaper",
                  "children": []
                },
                {
                  "name": "WithdrawnPaper",
                  "children": []
                },
                {
                  "name": "AcceptedPaper",
                  "children": []
                }
              ]
            }
          ]
        },
        {
          "name": "ConferenceEvent",
          "children": [
            {
              "name": "NonAcademicEvent",
              "children": [
                {
                  "name": "SocialEvent",
                  "children": [
                    {
                      "name": "Reception",
                      "children": []
                    },
                    {
                      "name": "Excursion",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "BreakEvent",
                  "children": [
                    {
                      "name": "CoffeeBreak",
                      "children": []
                    },
                    {
                      "name": "MealBreak",
                      "children": []
                    },
                    {
                      "name": "FreeTimeBreak",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "MealEvent",
                  "children": [
                    {
                      "name": "ConferenceDinner",
                      "children": []
                    }
                  ]
                }
              ]
            },
            {
              "name": "AcademicEvent",
              "children": [
                {
                  "name": "TalkEvent",
                  "children": [
                    {
                      "name": "WelcomeTalk",
                      "children": []
                    },
                    {
                      "name": "ClosingTalk",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "PaperPresentation",
                  "children": []
                },
                {
                  "name": "Workshop",
                  "children": []
                },
                {
                  "name": "OrganizationalMeeting",
                  "children": []
                }
              ]
            }
          ]
        },
        {
          "name": "ReviewRating",
          "children": [
            {
              "name": "AcceptRating",
              "children": []
            },
            {
              "name": "AccpetIfRoomRating",
              "children": []
            },
            {
              "name": "RejectRating",
              "children": []
            },
            {
              "name": "WeekRejectRating",
              "children": []
            }
          ]
        }
      ]
    }
  };

ekaw = {
    "name": "ekaw",
    "baseNS": "http://ekaw",
    "classCount": 73,
    "root": {
      "name": "Thing",
      "children": [
        {
          "name": "Organisation",
          "children": [
            {
              "name": "Academic_Institution",
              "children": [
                {
                  "name": "University",
                  "children": []
                },
                {
                  "name": "Research_Institute",
                  "children": []
                }
              ]
            },
            {
              "name": "Proceedings_Publisher",
              "children": []
            },
            {
              "name": "Organising_Agency",
              "children": []
            }
          ]
        },
        {
          "name": "Event",
          "children": [
            {
              "name": "Scientific_Event",
              "children": [
                {
                  "name": "Conference",
                  "children": []
                },
                {
                  "name": "Individual_Presentation",
                  "children": [
                    {
                      "name": "Tutorial",
                      "children": []
                    },
                    {
                      "name": "Contributed_Talk",
                      "children": []
                    },
                    {
                      "name": "Invited_Talk",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "Session",
                  "children": [
                    {
                      "name": "Conference_Session",
                      "children": [
                        {
                          "name": "Industrial_Session",
                          "children": []
                        }
                      ]
                    },
                    {
                      "name": "Poster_Session",
                      "children": []
                    },
                    {
                      "name": "Demo_Session",
                      "children": []
                    },
                    {
                      "name": "Workshop_Session",
                      "children": []
                    },
                    {
                      "name": "Regular_Session",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "Workshop",
                  "children": []
                },
                {
                  "name": "Track",
                  "children": []
                }
              ]
            },
            {
              "name": "Social_Event",
              "children": [
                {
                  "name": "Conference_Banquet",
                  "children": []
                },
                {
                  "name": "Conference_Trip",
                  "children": []
                }
              ]
            }
          ]
        },
        {
          "name": "Location",
          "children": []
        },
        {
          "name": "Person",
          "children": [
            {
              "name": "Paper_Author",
              "children": []
            },
            {
              "name": "Conference_Participant",
              "children": [
                {
                  "name": "Workshop_Chair",
                  "children": []
                },
                {
                  "name": "Session_Chair",
                  "children": []
                },
                {
                  "name": "Tutorial_Chair",
                  "children": []
                },
                {
                  "name": "Late-Registered_Participant",
                  "children": []
                },
                {
                  "name": "Demo_Chair",
                  "children": []
                },
                {
                  "name": "OC_Member",
                  "children": [
                    {
                      "name": "OC_Chair",
                      "children": []
                    }
                  ]
                },
                {
                  "name": "Early-Registered_Participant",
                  "children": []
                },
                {
                  "name": "PC_Chair",
                  "children": []
                },
                {
                  "name": "Presenter",
                  "children": [
                    {
                      "name": "Invited_Speaker",
                      "children": []
                    }
                  ]
                }
              ]
            },
            {
              "name": "Student",
              "children": []
            },
            {
              "name": "Possible_Reviewer",
              "children": [
                {
                  "name": "PC_Member",
                  "children": [
                    {
                      "name": "SC_Member",
                      "children": []
                    },
                    {
                      "name": "PC_Chair",
                      "children": []
                    },
                    {
                      "name": "Session_Chair",
                      "children": []
                    },
                    {
                      "name": "Workshop_Chair",
                      "children": []
                    },
                    {
                      "name": "Tutorial_Chair",
                      "children": []
                    }
                  ]
                }
              ]
            },
            {
              "name": "Agency_Staff_Member",
              "children": []
            }
          ]
        },
        {
          "name": "Research_Topic",
          "children": []
        },
        {
          "name": "Document",
          "children": [
            {
              "name": "Flyer",
              "children": []
            },
            {
              "name": "Abstract",
              "children": [
                {
                  "name": "Invited_Talk_Abstract",
                  "children": []
                },
                {
                  "name": "Tutorial_Abstract",
                  "children": []
                }
              ]
            },
            {
              "name": "Paper",
              "children": [
                {
                  "name": "Poster_Paper",
                  "children": []
                },
                {
                  "name": "Conference_Paper",
                  "children": []
                },
                {
                  "name": "Camera_Ready_Paper",
                  "children": []
                },
                {
                  "name": "Industrial_Paper",
                  "children": []
                },
                {
                  "name": "Regular_Paper",
                  "children": []
                },
                {
                  "name": "Demo_Paper",
                  "children": []
                },
                {
                  "name": "Submitted_Paper",
                  "children": [
                    {
                      "name": "Assigned_Paper",
                      "children": [
                        {
                          "name": "Evaluated_Paper",
                          "children": [
                            {
                              "name": "Rejected_Paper",
                              "children": []
                            },
                            {
                              "name": "Accepted_Paper",
                              "children": []
                            }
                          ]
                        }
                      ]
                    }
                  ]
                },
                {
                  "name": "Workshop_Paper",
                  "children": []
                }
              ]
            },
            {
              "name": "Multi-author_Volume",
              "children": [
                {
                  "name": "Proceedings",
                  "children": [
                    {
                      "name": "Conference_Proceedings",
                      "children": []
                    }
                  ]
                }
              ]
            },
            {
              "name": "Programme_Brochure",
              "children": []
            },
            {
              "name": "Web_Site",
              "children": []
            },
            {
              "name": "Review",
              "children": [
                {
                  "name": "Positive_Review",
                  "children": []
                },
                {
                  "name": "Neutral_Review",
                  "children": []
                },
                {
                  "name": "Negative_Review",
                  "children": []
                }
              ]
            }
          ]
        }
      ]
    }
  };

mapping_edas_ekaw = {
	"onto1": "http://edas",
	"onto2": "http://ekaw",
	"alignments": [
		{
			"entity1": "ConferenceDinner",
			"entity2": "Conference_Banquet"
		},
		{
			"entity1": "AcademicEvent",
			"entity2": "Scientific_Event"
		},
		{
			"entity1": "AcceptedPaper",
			"entity2": "Accepted_Paper"
    },
		// { // excluded the alignment for object property
		// 	"entity1": "isReviewedBy",
		// 	"entity2": "hasReviewer"
		// },
		{
			"entity1": "Place",
			"entity2": "Location"
		},
		{
			"entity1": "AcademiaOrganization",
			"entity2": "Academic_Institution"
		},
		{
			"entity1": "SocialEvent",
			"entity2": "Social_Event"
		},
		// { // excluded the alignment for object property
		// 	"entity1": "isReviewing",
		// 	"entity2": "reviewerOfPaper"
		// },
		{
			"entity1": "Organization",
			"entity2": "Organisation"
		},
		{
			"entity1": "Author",
			"entity2": "Paper_Author"
		},
		// { // excluded the alignment for object property
		// 	"entity1": "isLocationOf",
		// 	"entity2": "locationOf"
		// },
		{
			"entity1": "Topic",
			"entity2": "Research_Topic"
		},
		{
			"entity1": "Document",
			"entity2": "Document"
		},
		{
			"entity1": "RejectedPaper",
			"entity2": "Rejected_Paper"
		},
		{
			"entity1": "ConferenceEvent",
			"entity2": "Event"
		},
		{
			"entity1": "SessionChair",
			"entity2": "Session_Chair"
		},
		{
			"entity1": "Person",
			"entity2": "Person"
		},
		{
			"entity1": "Programme",
			"entity2": "Programme_Brochure"
		},
		{
			"entity1": "Review",
			"entity2": "Review"
		},
		{
			"entity1": "Workshop",
			"entity2": "Workshop"
		},
		{
			"entity1": "Paper",
			"entity2": "Paper"
		},
		{
			"entity1": "Attendee",
			"entity2": "Conference_Participant"
		},
		// { // excluded the alignment for object property
		// 	"entity1": "hasLocation",
		// 	"entity2": "heldIn"
		// }
	]
};