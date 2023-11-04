var edas = {
    "name": "edas",
    "baseNS": "http://edas",
    "classCount": 97, //103 - 6 (to match 15 mappings. sessionChair,review,socialEvent, programme got)
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