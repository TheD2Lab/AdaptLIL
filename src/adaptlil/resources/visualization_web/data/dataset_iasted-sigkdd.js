var iasted;
var sigkdd;
var mapping_iasted_sigkdd;

iasted = {
    "name": "iasted",
    "baseNS": "http://iasted#",
    "classCount": 140,
    "root": {
      "depth": 0,
      "name": "Thing",
      "children": [
        {
          "depth": 1,
          "name": "Money",
          "children": [
            {
              "depth": 2,
              "name": "Fee",
              "children": [
                {
                  "depth": 3,
                  "name": "Fee_for_extra_trip",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Registration_fee",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Student_registration_fee",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Memeber_registration_fee",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Nonauthor_registration_fee",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Author_attendee_cd_registration_fee",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Nonmember_registration_fee",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Author_attendee_book_registration_fee",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Hotel_fee",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Tax",
              "children": [
                {
                  "depth": 3,
                  "name": "Departure_tax",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Value_added_tax",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Sponzorship",
              "children": []
            },
            {
              "depth": 2,
              "name": "Tip",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Activity",
          "children": [
            {
              "depth": 2,
              "name": "Activity_after_conference",
              "children": [
                {
                  "depth": 3,
                  "name": "Full_day_tour",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Departure",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Activity_before_conference",
              "children": [
                {
                  "depth": 3,
                  "name": "Research",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Modelling",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Simulating",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Receiving_manuscript",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Accepting_manuscript",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Refusing_manuscript",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Renting",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Conference_activity",
              "children": [
                {
                  "depth": 3,
                  "name": "Social_program",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Dinner_banquet",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Welcome_address",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Coffee_break",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Registration",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Presentation",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Video_presentation",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "PowerPoint_presentation",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Coctail_reception",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Technic_activity",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Introduction",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Introduction_of_speaker",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Lecture",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Session",
                      "children": [
                        {
                          "depth": 5,
                          "name": "Speaker_lecture",
                          "children": []
                        }
                      ]
                    },
                    {
                      "depth": 4,
                      "name": "Tutorial",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Plenary_lecture",
                      "children": []
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          "depth": 1,
          "name": "State",
          "children": [
            {
              "depth": 2,
              "name": "Presenter_state",
              "children": []
            },
            {
              "depth": 2,
              "name": "Sponsor_state",
              "children": []
            },
            {
              "depth": 2,
              "name": "Conference_state",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Currency",
          "children": []
        },
        {
          "depth": 1,
          "name": "Person",
          "children": [
            {
              "depth": 2,
              "name": "Delegate",
              "children": [
                {
                  "depth": 3,
                  "name": "IASTED_member",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Non_speaker",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Student_non_speaker",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Worker_non_speaker",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Technical_commitee",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Listener",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "IASTED_non_member",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Hotel_presenter",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Speaker",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Reviewer",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Author",
                      "children": [
                        {
                          "depth": 5,
                          "name": "Lecturer",
                          "children": [
                            {
                              "depth": 6,
                              "name": "Student_lecturer",
                              "children": []
                            },
                            {
                              "depth": 6,
                              "name": "Worker_lecturer",
                              "children": []
                            }
                          ]
                        },
                        {
                          "depth": 5,
                          "name": "Tutorial_speaker",
                          "children": []
                        },
                        {
                          "depth": 5,
                          "name": "Plenary_lecture_speaker",
                          "children": []
                        },
                        {
                          "depth": 5,
                          "name": "Author_book_proceedings_included",
                          "children": []
                        },
                        {
                          "depth": 5,
                          "name": "Author_cd_proceedings_included",
                          "children": []
                        }
                      ]
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Session_chair",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Conference_Hiker",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "One_day_presenter",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Sponsor",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Time_zone",
          "children": []
        },
        {
          "depth": 1,
          "name": "Item",
          "children": [
            {
              "depth": 2,
              "name": "Audiovisual_equipment",
              "children": [
                {
                  "depth": 3,
                  "name": "Overhead_projector",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Computer",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "LCD_projector",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Video_cassette_player",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Card",
              "children": [
                {
                  "depth": 3,
                  "name": "Credit_card",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Transport_vehicle",
              "children": [
                {
                  "depth": 3,
                  "name": "Van",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Shuttle_bus",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Taxi",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Car",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Publication",
              "children": [
                {
                  "depth": 3,
                  "name": "Cd_proceening",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Book_proceeding",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Document",
              "children": [
                {
                  "depth": 3,
                  "name": "Form",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Author_information_form",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Registration_form",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Hotel_registration_form",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Brief_introduction_for_Session_chair",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Transparency",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Submission",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Initial_manuscipt",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Final_manuscript",
                      "children": []
                    }
                  ]
                },
                {
                  "depth": 3,
                  "name": "Invitation_letter",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Mailing_list",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Record_of_attendance",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Viza",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Review",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Payment_document",
                  "children": [
                    {
                      "depth": 4,
                      "name": "Bank_transfer",
                      "children": []
                    },
                    {
                      "depth": 4,
                      "name": "Cheque",
                      "children": []
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          "depth": 1,
          "name": "Time",
          "children": [
            {
              "depth": 2,
              "name": "Deadline",
              "children": [
                {
                  "depth": 3,
                  "name": "Camera_ready_manuscript_deadline",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Deadline_hotel_reservation",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Registation_deadline",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Deadline_for_notification_of_acceptance",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Submissions_deadline",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Conference_days",
              "children": [
                {
                  "depth": 3,
                  "name": "One_conference_day",
                  "children": []
                }
              ]
            },
            {
              "depth": 2,
              "name": "Trip_day",
              "children": []
            }
          ]
        }
      ]
    }
  };

sigkdd = {
    "name": "sigkdd",
    "baseNS": "http://sigkdd#",
    "classCount": 49,
    "root": {
      "depth": 0,
      "name": "Thing",
      "children": [
        {
          "depth": 1,
          "name": "Sponzor",
          "children": [
            {
              "depth": 2,
              "name": "Platinum_Supporter",
              "children": []
            },
            {
              "depth": 2,
              "name": "Gold_Supporter",
              "children": []
            },
            {
              "depth": 2,
              "name": "Best_Student_Paper_Supporter",
              "children": []
            },
            {
              "depth": 2,
              "name": "Silver_Supporter",
              "children": []
            },
            {
              "depth": 2,
              "name": "Bronze_Supporter",
              "children": []
            },
            {
              "depth": 2,
              "name": "Exhibitor",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Committee",
          "children": [
            {
              "depth": 2,
              "name": "Organizing_Committee",
              "children": []
            },
            {
              "depth": 2,
              "name": "Program_Committee",
              "children": []
            },
            {
              "depth": 2,
              "name": "Best_Paper_Awards_Committee",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Fee",
          "children": [
            {
              "depth": 2,
              "name": "Sponzor_fee",
              "children": []
            },
            {
              "depth": 2,
              "name": "Registration_fee",
              "children": [
                {
                  "depth": 3,
                  "name": "Registration_Student",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Registration_SIGKDD_Member",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Registration_Non-Member",
                  "children": []
                },
                {
                  "depth": 3,
                  "name": "Registration_SIGMOD_Member",
                  "children": []
                }
              ]
            }
          ]
        },
        {
          "depth": 1,
          "name": "Award",
          "children": [
            {
              "depth": 2,
              "name": "Best_Research_Paper_Award",
              "children": []
            },
            {
              "depth": 2,
              "name": "Best_Student_Paper_Award",
              "children": []
            },
            {
              "depth": 2,
              "name": "Best_Applications_Paper_Award",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Document",
          "children": [
            {
              "depth": 2,
              "name": "Paper",
              "children": []
            },
            {
              "depth": 2,
              "name": "Review",
              "children": []
            },
            {
              "depth": 2,
              "name": "Abstract",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Conference",
          "children": []
        },
        {
          "depth": 1,
          "name": "Deadline",
          "children": [
            {
              "depth": 2,
              "name": "Deadline_Abstract_Submission",
              "children": []
            },
            {
              "depth": 2,
              "name": "Deadline_Author_notification",
              "children": []
            },
            {
              "depth": 2,
              "name": "Deadline_Paper_Submission",
              "children": []
            }
          ]
        },
        {
          "depth": 1,
          "name": "Place",
          "children": [
            {
              "depth": 2,
              "name": "Main_office",
              "children": []
            },
            {
              "depth": 2,
              "name": "Conference_hall",
              "children": []
            },
            {
              "depth": 2,
              "name": "Hotel",
              "children": []
            }
          ]
        }
      ]
    }
  };

mapping_iasted_sigkdd = {
	"onto1": "http://iasted",
	"onto2": "http://sigkdd",
	"alignments": [
		{
			"entity1": "Place",
			"entity2": "Place"
		},
		{
			"entity1": "Review",
			"entity2": "Review"
		},
		{
			"entity1": "Student_registration_fee",
			"entity2": "Registration_Student"
		},
		{
			"entity1": "Fee",
			"entity2": "Fee"
		},
		{
			"entity1": "Registration_fee",
			"entity2": "Registration_fee"
		},
		{
			"entity1": "Sponsor",
			"entity2": "Sponzor"
		},
		{
			"entity1": "Deadline_for_notification_of_acceptance",
			"entity2": "Deadline_Author_notification"
		},
		{
			"entity1": "Nonmember_registration_fee",
			"entity2": "Registration_Non-Member"
		},
		{
			"entity1": "Author",
			"entity2": "Author"
		},
		{
			"entity1": "Listener",
			"entity2": "Listener"
		},
		{
			"entity1": "Main_office",
			"entity2": "Main_office"
		},
		{
			"entity1": "Conference_hall",
			"entity2": "Conference_hall"
		},
		{
			"entity1": "Person",
			"entity2": "Person"
		},
		{
			"entity1": "Deadline",
			"entity2": "Deadline"
		},
		{
			"entity1": "Speaker",
			"entity2": "Speaker"
		}
	]
};