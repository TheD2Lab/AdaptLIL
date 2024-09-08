var human;
var mouse;
var mapping_human_mouse;

human = {
  "name": "human",
  "baseNS": "http://human.owl#",
  "classCount": 119,
  "root": {
    "depth": 0,
    "name": "Thing",
    "uri": "",
    "children": [
      {
        "depth": 1,
        "name": "Organ",
        "uri": "http://human.owl#NCI_C13018",
        "children": [
          {
            "depth": 2,
            "name": "Bladder",
            "uri": "http://human.owl#NCI_C12414",
            "children": []
          },
          {
            "depth": 2,
            "name": "Heart",
            "uri": "http://human.owl#NCI_C12727",
            "children": []
          },
          {
            "depth": 2,
            "name": "Liver",
            "uri": "http://human.owl#NCI_C12392",
            "children": []
          },
          {
            "depth": 2,
            "name": "Breast",
            "uri": "http://human.owl#NCI_C12971",
            "children": []
          },
          {
            "depth": 2,
            "name": "Stomach",
            "uri": "http://human.owl#NCI_C12391",
            "children": []
          },
          {
            "depth": 2,
            "name": "Uterus",
            "uri": "http://human.owl#NCI_C12405",
            "children": [
              {
                "depth": 3,
                "name": "Pregnant_Uterus",
                "uri": "http://human.owl#NCI_C12406",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "Skin",
            "uri": "http://human.owl#NCI_C12470",
            "children": []
          },
          {
            "depth": 2,
            "name": "Vulva",
            "uri": "http://human.owl#NCI_C12408",
            "children": []
          },
          {
            "depth": 2,
            "name": "Lung",
            "uri": "http://human.owl#NCI_C12468",
            "children": [
              {
                "depth": 3,
                "name": "Right_Lung",
                "uri": "http://human.owl#NCI_C33483",
                "children": []
              },
              {
                "depth": 3,
                "name": "Left_Lung",
                "uri": "http://human.owl#NCI_C32967",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "Bone_Marrow",
            "uri": "http://human.owl#NCI_C12431",
            "children": []
          },
          {
            "depth": 2,
            "name": "Pancreas",
            "uri": "http://human.owl#NCI_C12393",
            "children": []
          },
          {
            "depth": 2,
            "name": "Tonsil",
            "uri": "http://human.owl#NCI_C12802",
            "children": []
          },
          {
            "depth": 2,
            "name": "Vagina",
            "uri": "http://human.owl#NCI_C12407",
            "children": []
          },
          {
            "depth": 2,
            "name": "Kidney",
            "uri": "http://human.owl#NCI_C12415",
            "children": [
              {
                "depth": 3,
                "name": "Left_Kidney",
                "uri": "http://human.owl#NCI_C34006",
                "children": []
              },
              {
                "depth": 3,
                "name": "Right_Kidney",
                "uri": "http://human.owl#NCI_C34005",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "Vas_Deferens",
            "uri": "http://human.owl#NCI_C12813",
            "children": []
          },
          {
            "depth": 2,
            "name": "Bronchial_Tree",
            "uri": "http://human.owl#NCI_C32233",
            "children": []
          },
          {
            "depth": 2,
            "name": "Testis",
            "uri": "http://human.owl#NCI_C12412",
            "children": []
          },
          {
            "depth": 2,
            "name": "Intestine",
            "uri": "http://human.owl#NCI_C12736",
            "children": []
          },
          {
            "depth": 2,
            "name": "Penis",
            "uri": "http://human.owl#NCI_C12409",
            "children": []
          },
          {
            "depth": 2,
            "name": "Urethra",
            "uri": "http://human.owl#NCI_C12417",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "Body_Fluid_or_Substance",
        "uri": "http://human.owl#NCI_C13236",
        "children": [
          {
            "depth": 2,
            "name": "Exocrine_Gland_Fluid_or_Secretion",
            "uri": "http://human.owl#NCI_C34062",
            "children": [
              {
                "depth": 3,
                "name": "Tear",
                "uri": "http://human.owl#NCI_C33739",
                "children": []
              },
              {
                "depth": 3,
                "name": "Saliva",
                "uri": "http://human.owl#NCI_C13275",
                "children": []
              },
              {
                "depth": 3,
                "name": "Mucus",
                "uri": "http://human.owl#NCI_C13259",
                "children": [
                  {
                    "depth": 4,
                    "name": "Nasal_Mucus",
                    "uri": "http://human.owl#NCI_C52551",
                    "children": []
                  }
                ]
              }
            ]
          },
          {
            "depth": 2,
            "name": "Blood",
            "uri": "http://human.owl#NCI_C12434",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "Body_Part",
        "uri": "http://human.owl#NCI_C32221",
        "children": [
          {
            "depth": 2,
            "name": "Extremity_Part",
            "uri": "http://human.owl#NCI_C38630",
            "children": [
              {
                "depth": 3,
                "name": "Digit",
                "uri": "http://human.owl#NCI_C40186",
                "children": [
                  {
                    "depth": 4,
                    "name": "Toe",
                    "uri": "http://human.owl#NCI_C33788",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Finger",
                    "uri": "http://human.owl#NCI_C32608",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Lower_Extremity_Part",
                "uri": "http://human.owl#NCI_C38629",
                "children": [
                  {
                    "depth": 4,
                    "name": "Toe",
                    "uri": "http://human.owl#NCI_C33788",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Thigh",
                    "uri": "http://human.owl#NCI_C33763",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Pelvic_Girdle",
                    "uri": "http://human.owl#NCI_C33291",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Leg",
                    "uri": "http://human.owl#NCI_C32974",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Foot",
                    "uri": "http://human.owl#NCI_C32622",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Dorsal_Region_of_Foot",
                    "uri": "http://human.owl#NCI_C32476",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Upper_Extremity_Part",
                "uri": "http://human.owl#NCI_C38628",
                "children": [
                  {
                    "depth": 4,
                    "name": "Hand",
                    "uri": "http://human.owl#NCI_C32712",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Forearm",
                    "uri": "http://human.owl#NCI_C32628",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Finger",
                    "uri": "http://human.owl#NCI_C32608",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Arm",
                    "uri": "http://human.owl#NCI_C32141",
                    "children": []
                  }
                ]
              }
            ]
          },
          {
            "depth": 2,
            "name": "Organ_of_Special_Sense_Part",
            "uri": "http://human.owl#NCI_C38626",
            "children": [
              {
                "depth": 3,
                "name": "Nostril",
                "uri": "http://human.owl#NCI_C33178",
                "children": []
              },
              {
                "depth": 3,
                "name": "External_Nose",
                "uri": "http://human.owl#NCI_C32566",
                "children": []
              },
              {
                "depth": 3,
                "name": "Dorsum_of_the_Tongue",
                "uri": "http://human.owl#NCI_C32482",
                "children": []
              },
              {
                "depth": 3,
                "name": "Apex_of_the_Tongue",
                "uri": "http://human.owl#NCI_C32130",
                "children": []
              },
              {
                "depth": 3,
                "name": "Base_of_the_Tongue",
                "uri": "http://human.owl#NCI_C12228",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "Tissue_Part",
            "uri": "http://human.owl#NCI_C38625",
            "children": []
          },
          {
            "depth": 2,
            "name": "Integumentary_System_Part",
            "uri": "http://human.owl#NCI_C38620",
            "children": [
              {
                "depth": 3,
                "name": "Hair_Part",
                "uri": "http://human.owl#NCI_C38465",
                "children": []
              },
              {
                "depth": 3,
                "name": "Nail",
                "uri": "http://human.owl#NCI_C33156",
                "children": [
                  {
                    "depth": 4,
                    "name": "Toe_Nail",
                    "uri": "http://human.owl#NCI_C33790",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Fingernail",
                    "uri": "http://human.owl#NCI_C32609",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Hair",
                "uri": "http://human.owl#NCI_C32705",
                "children": [
                  {
                    "depth": 4,
                    "name": "Pubic_Hair",
                    "uri": "http://human.owl#NCI_C33424",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Eyelash",
                    "uri": "http://human.owl#NCI_C32576",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Skin_Part",
                "uri": "http://human.owl#NCI_C13022",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "Head_and_Neck_Part",
            "uri": "http://human.owl#NCI_C38617",
            "children": [
              {
                "depth": 3,
                "name": "Eye_Appendage",
                "uri": "http://human.owl#NCI_C32574",
                "children": [
                  {
                    "depth": 4,
                    "name": "Eyebrow",
                    "uri": "http://human.owl#NCI_C32575",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Eyelid",
                    "uri": "http://human.owl#NCI_C12713",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Cheek",
                "uri": "http://human.owl#NCI_C13070",
                "children": []
              },
              {
                "depth": 3,
                "name": "Tooth",
                "uri": "http://human.owl#NCI_C12506",
                "children": [
                  {
                    "depth": 4,
                    "name": "Vital_Tooth",
                    "uri": "http://human.owl#NCI_C52669",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Temporary_Tooth",
                    "uri": "http://human.owl#NCI_C33745",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Permanent_Tooth",
                    "uri": "http://human.owl#NCI_C33313",
                    "children": [
                      {
                        "depth": 5,
                        "name": "Molar_Tooth",
                        "uri": "http://human.owl#NCI_C33136",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Incisor",
                        "uri": "http://human.owl#NCI_C32769",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Canine_Tooth",
                        "uri": "http://human.owl#NCI_C32258",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Bicuspid_Tooth",
                        "uri": "http://human.owl#NCI_C32201",
                        "children": []
                      }
                    ]
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Uvula",
                "uri": "http://human.owl#NCI_C12232",
                "children": []
              },
              {
                "depth": 3,
                "name": "External_Lip",
                "uri": "http://human.owl#NCI_C12223",
                "children": []
              },
              {
                "depth": 3,
                "name": "Lip",
                "uri": "http://human.owl#NCI_C12220",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "Sinus",
            "uri": "http://human.owl#NCI_C33556",
            "children": []
          },
          {
            "depth": 2,
            "name": "Musculoskeletal_System_Part",
            "uri": "http://human.owl#NCI_C25769",
            "children": [
              {
                "depth": 3,
                "name": "Muscle",
                "uri": "http://human.owl#NCI_C13056",
                "children": []
              },
              {
                "depth": 3,
                "name": "Joint",
                "uri": "http://human.owl#NCI_C13044",
                "children": []
              },
              {
                "depth": 3,
                "name": "Skeletal_System",
                "uri": "http://human.owl#NCI_C12788",
                "children": []
              },
              {
                "depth": 3,
                "name": "Base_of_the_Skull",
                "uri": "http://human.owl#NCI_C12493",
                "children": []
              },
              {
                "depth": 3,
                "name": "Cartilage",
                "uri": "http://human.owl#NCI_C12373",
                "children": []
              },
              {
                "depth": 3,
                "name": "Bone",
                "uri": "http://human.owl#NCI_C12366",
                "children": [
                  {
                    "depth": 4,
                    "name": "Pelvic_Bone",
                    "uri": "http://human.owl#NCI_C33287",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Irregular_Bone",
                    "uri": "http://human.owl#NCI_C32881",
                    "children": [
                      {
                        "depth": 5,
                        "name": "Superior_Maxillary_Bone",
                        "uri": "http://human.owl#NCI_C33682",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Vertebral_Bone",
                        "uri": "http://human.owl#NCI_C33868",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Maxilla",
                        "uri": "http://human.owl#NCI_C26470",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Jaw",
                        "uri": "http://human.owl#NCI_C48821",
                        "children": [
                          {
                            "depth": 6,
                            "name": "Lower_Jaw",
                            "uri": "http://human.owl#NCI_C48823",
                            "children": []
                          },
                          {
                            "depth": 6,
                            "name": "Upper_Jaw",
                            "uri": "http://human.owl#NCI_C48822",
                            "children": []
                          }
                        ]
                      },
                      {
                        "depth": 5,
                        "name": "Sphenoid_Bone",
                        "uri": "http://human.owl#NCI_C12790",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Temporal_Bone",
                        "uri": "http://human.owl#NCI_C12797",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Hyoid_Bone",
                        "uri": "http://human.owl#NCI_C32752",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Ethmoid_Bone",
                        "uri": "http://human.owl#NCI_C12711",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Mandible",
                        "uri": "http://human.owl#NCI_C12290",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Sacral_Bone",
                        "uri": "http://human.owl#NCI_C33504",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Inferior_Turbinate",
                        "uri": "http://human.owl#NCI_C32794",
                        "children": []
                      }
                    ]
                  },
                  {
                    "depth": 4,
                    "name": "Flat_Bone",
                    "uri": "http://human.owl#NCI_C32616",
                    "children": [
                      {
                        "depth": 5,
                        "name": "Zygomatic_Arch",
                        "uri": "http://human.owl#NCI_C33897",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Nasal_Bone",
                        "uri": "http://human.owl#NCI_C33157",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Lacrimal_Bone",
                        "uri": "http://human.owl#NCI_C32906",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Frontal_Bone",
                        "uri": "http://human.owl#NCI_C32635",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Sternum",
                        "uri": "http://human.owl#NCI_C12793",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Rib",
                        "uri": "http://human.owl#NCI_C12782",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Parietal_Bone",
                        "uri": "http://human.owl#NCI_C12766",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Occipital_Bone",
                        "uri": "http://human.owl#NCI_C12757",
                        "children": []
                      }
                    ]
                  },
                  {
                    "depth": 4,
                    "name": "Skull",
                    "uri": "http://human.owl#NCI_C12789",
                    "children": []
                  }
                ]
              }
            ]
          },
          {
            "depth": 2,
            "name": "Urinary_System_Part",
            "uri": "http://human.owl#NCI_C13035",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "Other_Anatomic_Concept",
        "uri": "http://human.owl#NCI_C33904",
        "children": [
          {
            "depth": 2,
            "name": "Viscera",
            "uri": "http://human.owl#NCI_C28287",
            "children": []
          },
          {
            "depth": 2,
            "name": "Genitalia",
            "uri": "http://human.owl#NCI_C25177",
            "children": []
          },
          {
            "depth": 2,
            "name": "Aspirate",
            "uri": "http://human.owl#NCI_C13347",
            "children": []
          },
          {
            "depth": 2,
            "name": "Body",
            "uri": "http://human.owl#NCI_C13041",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Thigh",
            "uri": "http://human.owl#NCI_C33763",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Eyelash",
            "uri": "http://human.owl#NCI_C32576",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Cartilage",
            "uri": "http://human.owl#NCI_C12373",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Temporal_Bone",
            "uri": "http://human.owl#NCI_C12797",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Finger",
            "uri": "http://human.owl#NCI_C32608",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Fingernail",
            "uri": "http://human.owl#NCI_C32609",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Parietal_Bone",
            "uri": "http://human.owl#NCI_C12766",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Frontal_Bone",
            "uri": "http://human.owl#NCI_C32635",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Toe_Nail",
            "uri": "http://human.owl#NCI_C33790",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Joint",
            "uri": "http://human.owl#NCI_C13044",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Sphenoid_Bone",
            "uri": "http://human.owl#NCI_C12790",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Toe",
            "uri": "http://human.owl#NCI_C33788",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Maxilla",
            "uri": "http://human.owl#NCI_C26470",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Jaw",
            "uri": "http://human.owl#NCI_C48821",
            "children": [
              {
                "depth": 3,
                "name": "Lower_Jaw",
                "uri": "http://human.owl#NCI_C48823",
                "children": []
              },
              {
                "depth": 3,
                "name": "Upper_Jaw",
                "uri": "http://human.owl#NCI_C48822",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Hyoid_Bone",
            "uri": "http://human.owl#NCI_C32752",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Base_of_the_Skull",
            "uri": "http://human.owl#NCI_C12493",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "External_Lip",
            "uri": "http://human.owl#NCI_C12223",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Ethmoid_Bone",
            "uri": "http://human.owl#NCI_C12711",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Dorsal_Region_of_Foot",
            "uri": "http://human.owl#NCI_C32476",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Occipital_Bone",
            "uri": "http://human.owl#NCI_C12757",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "Bone",
            "uri": "http://human.owl#NCI_C12366",
            "children": [
              {
                "depth": 3,
                "name": "Pelvic_Bone",
                "uri": "http://human.owl#NCI_C33287",
                "children": []
              },
              {
                "depth": 3,
                "name": "Irregular_Bone",
                "uri": "http://human.owl#NCI_C32881",
                "children": [
                  {
                    "depth": 4,
                    "name": "Superior_Maxillary_Bone",
                    "uri": "http://human.owl#NCI_C33682",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Vertebral_Bone",
                    "uri": "http://human.owl#NCI_C33868",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Maxilla",
                    "uri": "http://human.owl#NCI_C26470",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Jaw",
                    "uri": "http://human.owl#NCI_C48821",
                    "children": [
                      {
                        "depth": 5,
                        "name": "Lower_Jaw",
                        "uri": "http://human.owl#NCI_C48823",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "Upper_Jaw",
                        "uri": "http://human.owl#NCI_C48822",
                        "children": []
                      }
                    ]
                  },
                  {
                    "depth": 4,
                    "name": "Sphenoid_Bone",
                    "uri": "http://human.owl#NCI_C12790",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Temporal_Bone",
                    "uri": "http://human.owl#NCI_C12797",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Hyoid_Bone",
                    "uri": "http://human.owl#NCI_C32752",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Ethmoid_Bone",
                    "uri": "http://human.owl#NCI_C12711",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Mandible",
                    "uri": "http://human.owl#NCI_C12290",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Sacral_Bone",
                    "uri": "http://human.owl#NCI_C33504",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Inferior_Turbinate",
                    "uri": "http://human.owl#NCI_C32794",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Flat_Bone",
                "uri": "http://human.owl#NCI_C32616",
                "children": [
                  {
                    "depth": 4,
                    "name": "Zygomatic_Arch",
                    "uri": "http://human.owl#NCI_C33897",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Nasal_Bone",
                    "uri": "http://human.owl#NCI_C33157",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Lacrimal_Bone",
                    "uri": "http://human.owl#NCI_C32906",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Frontal_Bone",
                    "uri": "http://human.owl#NCI_C32635",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Sternum",
                    "uri": "http://human.owl#NCI_C12793",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Rib",
                    "uri": "http://human.owl#NCI_C12782",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Parietal_Bone",
                    "uri": "http://human.owl#NCI_C12766",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "Occipital_Bone",
                    "uri": "http://human.owl#NCI_C12757",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "Skull",
                "uri": "http://human.owl#NCI_C12789",
                "children": []
              }
            ]
          }
        ]
      }
    ]
  }
};

mouse = {
  "name": "mouse",
  "baseNS": "http://mouse.owl#",
  "classCount": 175,
  "root": {
    "depth": 0,
    "name": "Thing",
    "uri": "",
    "children": [
      {
        "depth": 1,
        "name": "iris",
        "uri": "http://mouse.owl#MA_0000273",
        "children": []
      },
      {
        "depth": 1,
        "name": "muscle",
        "uri": "http://mouse.owl#MA_0000015",
        "children": [
          {
            "depth": 2,
            "name": "tail muscle",
            "uri": "http://mouse.owl#MA_0000699",
            "children": []
          },
          {
            "depth": 2,
            "name": "limb muscle",
            "uri": "http://mouse.owl#MA_0000692",
            "children": [
              {
                "depth": 3,
                "name": "hindlimb muscle",
                "uri": "http://mouse.owl#MA_0000663",
                "children": [
                  {
                    "depth": 4,
                    "name": "leg muscle",
                    "uri": "http://mouse.owl#MA_0000672",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hip muscle",
                    "uri": "http://mouse.owl#MA_0000657",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "foot muscle",
                    "uri": "http://mouse.owl#MA_0000652",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "forelimb muscle",
                "uri": "http://mouse.owl#MA_0000615",
                "children": [
                  {
                    "depth": 4,
                    "name": "shoulder muscle",
                    "uri": "http://mouse.owl#MA_0000633",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hand muscle",
                    "uri": "http://mouse.owl#MA_0000628",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "arm muscle",
                    "uri": "http://mouse.owl#MA_0000594",
                    "children": []
                  }
                ]
              }
            ]
          },
          {
            "depth": 2,
            "name": "head/neck muscle",
            "uri": "http://mouse.owl#MA_0000571",
            "children": [
              {
                "depth": 3,
                "name": "neck muscle",
                "uri": "http://mouse.owl#MA_0000587",
                "children": []
              },
              {
                "depth": 3,
                "name": "head muscle",
                "uri": "http://mouse.owl#MA_0000578",
                "children": [
                  {
                    "depth": 4,
                    "name": "cranial/facial muscle",
                    "uri": "http://mouse.owl#MA_0000579",
                    "children": []
                  }
                ]
              }
            ]
          },
          {
            "depth": 2,
            "name": "trunk muscle",
            "uri": "http://mouse.owl#MA_0000514",
            "children": [
              {
                "depth": 3,
                "name": "chest muscle",
                "uri": "http://mouse.owl#MA_0000548",
                "children": []
              },
              {
                "depth": 3,
                "name": "pelvis muscle",
                "uri": "http://mouse.owl#MA_0000534",
                "children": []
              },
              {
                "depth": 3,
                "name": "abdomen muscle",
                "uri": "http://mouse.owl#MA_0000520",
                "children": []
              },
              {
                "depth": 3,
                "name": "back muscle",
                "uri": "http://mouse.owl#MA_0000496",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "smooth muscle",
            "uri": "http://mouse.owl#MA_0000166",
            "children": []
          },
          {
            "depth": 2,
            "name": "skeletal muscle",
            "uri": "http://mouse.owl#MA_0000165",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "placenta",
        "uri": "http://mouse.owl#MA_0000386",
        "children": []
      },
      {
        "depth": 1,
        "name": "pelvis",
        "uri": "http://mouse.owl#MA_0000030",
        "children": []
      },
      {
        "depth": 1,
        "name": "intestine wall",
        "uri": "http://mouse.owl#MA_0002693",
        "children": []
      },
      {
        "depth": 1,
        "name": "white matter",
        "uri": "http://mouse.owl#MA_0001135",
        "children": [
          {
            "depth": 2,
            "name": "spinal cord white matter",
            "uri": "http://mouse.owl#MA_0000910",
            "children": []
          },
          {
            "depth": 2,
            "name": "brain white matter",
            "uri": "http://mouse.owl#MA_0000820",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "mouse anatomy",
        "uri": "http://mouse.owl#MA_0000001",
        "children": []
      },
      {
        "depth": 1,
        "name": "eyelash",
        "uri": "http://mouse.owl#MA_0002702",
        "children": []
      },
      {
        "depth": 1,
        "name": "body fluid/substance",
        "uri": "http://mouse.owl#MA_0002450",
        "children": [
          {
            "depth": 2,
            "name": "urine",
            "uri": "http://mouse.owl#MA_0002545",
            "children": []
          },
          {
            "depth": 2,
            "name": "skin fluid/secretion",
            "uri": "http://mouse.owl#MA_0002537",
            "children": [
              {
                "depth": 3,
                "name": "sweat",
                "uri": "http://mouse.owl#MA_0002539",
                "children": []
              },
              {
                "depth": 3,
                "name": "sebum",
                "uri": "http://mouse.owl#MA_0002538",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "respiratory system fluid/secretion",
            "uri": "http://mouse.owl#MA_0002533",
            "children": [
              {
                "depth": 3,
                "name": "nasal mucus",
                "uri": "http://mouse.owl#MA_0002535",
                "children": []
              },
              {
                "depth": 3,
                "name": "lung surfactant",
                "uri": "http://mouse.owl#MA_0002534",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "lymph",
            "uri": "http://mouse.owl#MA_0002520",
            "children": []
          },
          {
            "depth": 2,
            "name": "feces",
            "uri": "http://mouse.owl#MA_0002509",
            "children": []
          },
          {
            "depth": 2,
            "name": "blood",
            "uri": "http://mouse.owl#MA_0000059",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "joint",
        "uri": "http://mouse.owl#MA_0000319",
        "children": []
      },
      {
        "depth": 1,
        "name": "choroid",
        "uri": "http://mouse.owl#MA_0000263",
        "children": []
      },
      {
        "depth": 1,
        "name": "skin",
        "uri": "http://mouse.owl#MA_0000151",
        "children": [
          {
            "depth": 2,
            "name": "tail skin",
            "uri": "http://mouse.owl#MA_0000700",
            "children": []
          },
          {
            "depth": 2,
            "name": "limb skin",
            "uri": "http://mouse.owl#MA_0000694",
            "children": [
              {
                "depth": 3,
                "name": "hindlimb skin",
                "uri": "http://mouse.owl#MA_0000665",
                "children": [
                  {
                    "depth": 4,
                    "name": "leg skin",
                    "uri": "http://mouse.owl#MA_0000674",
                    "children": [
                      {
                        "depth": 5,
                        "name": "upper leg skin",
                        "uri": "http://mouse.owl#MA_0000686",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "lower leg skin",
                        "uri": "http://mouse.owl#MA_0000680",
                        "children": []
                      }
                    ]
                  },
                  {
                    "depth": 4,
                    "name": "knee skin",
                    "uri": "http://mouse.owl#MA_0000668",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hip skin",
                    "uri": "http://mouse.owl#MA_0000658",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "foot skin",
                    "uri": "http://mouse.owl#MA_0000654",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "ankle skin",
                    "uri": "http://mouse.owl#MA_0000641",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "forelimb skin",
                "uri": "http://mouse.owl#MA_0000617",
                "children": [
                  {
                    "depth": 4,
                    "name": "wrist skin",
                    "uri": "http://mouse.owl#MA_0000638",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "shoulder skin",
                    "uri": "http://mouse.owl#MA_0000635",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hand skin",
                    "uri": "http://mouse.owl#MA_0000630",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "arm skin",
                    "uri": "http://mouse.owl#MA_0000596",
                    "children": [
                      {
                        "depth": 5,
                        "name": "elbow skin",
                        "uri": "http://mouse.owl#MA_0000610",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "upper arm skin",
                        "uri": "http://mouse.owl#MA_0000608",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "lower arm skin",
                        "uri": "http://mouse.owl#MA_0000602",
                        "children": []
                      }
                    ]
                  }
                ]
              }
            ]
          },
          {
            "depth": 2,
            "name": "head/neck skin",
            "uri": "http://mouse.owl#MA_0000574",
            "children": [
              {
                "depth": 3,
                "name": "neck skin",
                "uri": "http://mouse.owl#MA_0000590",
                "children": []
              },
              {
                "depth": 3,
                "name": "head skin",
                "uri": "http://mouse.owl#MA_0000582",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "trunk skin",
            "uri": "http://mouse.owl#MA_0000517",
            "children": [
              {
                "depth": 3,
                "name": "thorax skin",
                "uri": "http://mouse.owl#MA_0000564",
                "children": [
                  {
                    "depth": 4,
                    "name": "chest skin",
                    "uri": "http://mouse.owl#MA_0000553",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "upper back skin",
                    "uri": "http://mouse.owl#MA_0000510",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "abdomen/pelvis/perineum skin",
                "uri": "http://mouse.owl#MA_0000530",
                "children": [
                  {
                    "depth": 4,
                    "name": "pelvis skin",
                    "uri": "http://mouse.owl#MA_0000546",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "abdomen skin",
                    "uri": "http://mouse.owl#MA_0000523",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "lower back skin",
                    "uri": "http://mouse.owl#MA_0000504",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "back skin",
                "uri": "http://mouse.owl#MA_0000498",
                "children": [
                  {
                    "depth": 4,
                    "name": "upper back skin",
                    "uri": "http://mouse.owl#MA_0000510",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "lower back skin",
                    "uri": "http://mouse.owl#MA_0000504",
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
        "name": "cuticle",
        "uri": "http://mouse.owl#MA_0002704",
        "children": []
      },
      {
        "depth": 1,
        "name": "lens",
        "uri": "http://mouse.owl#MA_0000275",
        "children": []
      },
      {
        "depth": 1,
        "name": "unfertilized egg",
        "uri": "http://mouse.owl#MA_0000388",
        "children": []
      },
      {
        "depth": 1,
        "name": "ear skin",
        "uri": "http://mouse.owl#MA_0001233",
        "children": []
      },
      {
        "depth": 1,
        "name": "anatomic region",
        "uri": "http://mouse.owl#MA_0002433",
        "children": [
          {
            "depth": 2,
            "name": "limb",
            "uri": "http://mouse.owl#MA_0000007",
            "children": [
              {
                "depth": 3,
                "name": "hindlimb",
                "uri": "http://mouse.owl#MA_0000026",
                "children": []
              },
              {
                "depth": 3,
                "name": "forelimb",
                "uri": "http://mouse.owl#MA_0000025",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "head/neck",
            "uri": "http://mouse.owl#MA_0000006",
            "children": []
          },
          {
            "depth": 2,
            "name": "trunk",
            "uri": "http://mouse.owl#MA_0000004",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "name": "vertebra",
        "uri": "http://mouse.owl#MA_0000309",
        "children": []
      },
      {
        "depth": 1,
        "name": "rectum",
        "uri": "http://mouse.owl#MA_0000336",
        "children": []
      },
      {
        "depth": 1,
        "name": "cranium",
        "uri": "http://mouse.owl#MA_0000316",
        "children": []
      },
      {
        "depth": 1,
        "name": "organ system",
        "uri": "http://mouse.owl#MA_0000003",
        "children": [
          {
            "depth": 2,
            "name": "immune system",
            "uri": "http://mouse.owl#MA_0002711",
            "children": []
          },
          {
            "depth": 2,
            "name": "sensory organ system",
            "uri": "http://mouse.owl#MA_0002442",
            "children": [
              {
                "depth": 3,
                "name": "gustatory system",
                "uri": "http://mouse.owl#MA_0002446",
                "children": []
              },
              {
                "depth": 3,
                "name": "olfactory system",
                "uri": "http://mouse.owl#MA_0002445",
                "children": []
              },
              {
                "depth": 3,
                "name": "visual system",
                "uri": "http://mouse.owl#MA_0002444",
                "children": []
              },
              {
                "depth": 3,
                "name": "auditory system",
                "uri": "http://mouse.owl#MA_0002443",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "visceral organ system",
            "uri": "http://mouse.owl#MA_0000019",
            "children": [
              {
                "depth": 3,
                "name": "digestive system",
                "uri": "http://mouse.owl#MA_0002431",
                "children": []
              },
              {
                "depth": 3,
                "name": "respiratory system",
                "uri": "http://mouse.owl#MA_0000327",
                "children": []
              },
              {
                "depth": 3,
                "name": "reproductive system",
                "uri": "http://mouse.owl#MA_0000326",
                "children": []
              },
              {
                "depth": 3,
                "name": "renal/urinary system",
                "uri": "http://mouse.owl#MA_0000325",
                "children": []
              },
              {
                "depth": 3,
                "name": "hepatobiliary system",
                "uri": "http://mouse.owl#MA_0000324",
                "children": []
              },
              {
                "depth": 3,
                "name": "gastrointestinal system",
                "uri": "http://mouse.owl#MA_0000323",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "nervous system",
            "uri": "http://mouse.owl#MA_0000016",
            "children": []
          },
          {
            "depth": 2,
            "name": "connective tissue",
            "uri": "http://mouse.owl#MA_0000011",
            "children": [
              {
                "depth": 3,
                "name": "head/neck connective tissue",
                "uri": "http://mouse.owl#MA_0000570",
                "children": [
                  {
                    "depth": 4,
                    "name": "neck connective tissue",
                    "uri": "http://mouse.owl#MA_0000585",
                    "children": [
                      {
                        "depth": 5,
                        "name": "neck cartilage",
                        "uri": "http://mouse.owl#MA_0000586",
                        "children": []
                      }
                    ]
                  },
                  {
                    "depth": 4,
                    "name": "head connective tissue",
                    "uri": "http://mouse.owl#MA_0000577",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "tendon",
                "uri": "http://mouse.owl#MA_0000115",
                "children": []
              },
              {
                "depth": 3,
                "name": "ligament",
                "uri": "http://mouse.owl#MA_0000113",
                "children": []
              },
              {
                "depth": 3,
                "name": "cartilage",
                "uri": "http://mouse.owl#MA_0000104",
                "children": []
              }
            ]
          },
          {
            "depth": 2,
            "name": "cardiovascular system",
            "uri": "http://mouse.owl#MA_0000010",
            "children": [
              {
                "depth": 3,
                "name": "vascular system",
                "uri": "http://mouse.owl#MA_0002718",
                "children": [
                  {
                    "depth": 4,
                    "name": "venous system",
                    "uri": "http://mouse.owl#MA_0002720",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "arterial system",
                    "uri": "http://mouse.owl#MA_0002719",
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
        "children": [
          {
            "depth": 2,
            "name": "ear",
            "uri": "http://mouse.owl#MA_0000236",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "leg",
            "uri": "http://mouse.owl#MA_0000047",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "neck organ",
            "uri": "http://mouse.owl#MA_0000589",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head",
            "uri": "http://mouse.owl#MA_0000023",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "limb skin",
            "uri": "http://mouse.owl#MA_0000694",
            "children": [
              {
                "depth": 3,
                "name": "hindlimb skin",
                "uri": "http://mouse.owl#MA_0000665",
                "children": [
                  {
                    "depth": 4,
                    "name": "leg skin",
                    "uri": "http://mouse.owl#MA_0000674",
                    "children": [
                      {
                        "depth": 5,
                        "name": "upper leg skin",
                        "uri": "http://mouse.owl#MA_0000686",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "lower leg skin",
                        "uri": "http://mouse.owl#MA_0000680",
                        "children": []
                      }
                    ]
                  },
                  {
                    "depth": 4,
                    "name": "knee skin",
                    "uri": "http://mouse.owl#MA_0000668",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hip skin",
                    "uri": "http://mouse.owl#MA_0000658",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "foot skin",
                    "uri": "http://mouse.owl#MA_0000654",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "ankle skin",
                    "uri": "http://mouse.owl#MA_0000641",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "forelimb skin",
                "uri": "http://mouse.owl#MA_0000617",
                "children": [
                  {
                    "depth": 4,
                    "name": "wrist skin",
                    "uri": "http://mouse.owl#MA_0000638",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "shoulder skin",
                    "uri": "http://mouse.owl#MA_0000635",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hand skin",
                    "uri": "http://mouse.owl#MA_0000630",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "arm skin",
                    "uri": "http://mouse.owl#MA_0000596",
                    "children": [
                      {
                        "depth": 5,
                        "name": "elbow skin",
                        "uri": "http://mouse.owl#MA_0000610",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "upper arm skin",
                        "uri": "http://mouse.owl#MA_0000608",
                        "children": []
                      },
                      {
                        "depth": 5,
                        "name": "lower arm skin",
                        "uri": "http://mouse.owl#MA_0000602",
                        "children": []
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "nose",
            "uri": "http://mouse.owl#MA_0000281",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "elbow skin",
            "uri": "http://mouse.owl#MA_0000610",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "eye",
            "uri": "http://mouse.owl#MA_0000261",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "neck connective tissue",
            "uri": "http://mouse.owl#MA_0000585",
            "children": [
              {
                "depth": 3,
                "name": "neck cartilage",
                "uri": "http://mouse.owl#MA_0000586",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "arm",
            "uri": "http://mouse.owl#MA_0000033",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hip",
            "uri": "http://mouse.owl#MA_0000045",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "limb muscle",
            "uri": "http://mouse.owl#MA_0000692",
            "children": [
              {
                "depth": 3,
                "name": "hindlimb muscle",
                "uri": "http://mouse.owl#MA_0000663",
                "children": [
                  {
                    "depth": 4,
                    "name": "leg muscle",
                    "uri": "http://mouse.owl#MA_0000672",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hip muscle",
                    "uri": "http://mouse.owl#MA_0000657",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "foot muscle",
                    "uri": "http://mouse.owl#MA_0000652",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "forelimb muscle",
                "uri": "http://mouse.owl#MA_0000615",
                "children": [
                  {
                    "depth": 4,
                    "name": "shoulder muscle",
                    "uri": "http://mouse.owl#MA_0000633",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hand muscle",
                    "uri": "http://mouse.owl#MA_0000628",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "arm muscle",
                    "uri": "http://mouse.owl#MA_0000594",
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
        "children": [
          {
            "depth": 2,
            "name": "tongue",
            "uri": "http://mouse.owl#MA_0000347",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hepatobiliary system",
            "uri": "http://mouse.owl#MA_0000324",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hip muscle",
            "uri": "http://mouse.owl#MA_0000657",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "limb joint",
            "uri": "http://mouse.owl#MA_0000691",
            "children": [
              {
                "depth": 3,
                "name": "hindlimb joint",
                "uri": "http://mouse.owl#MA_0000662",
                "children": [
                  {
                    "depth": 4,
                    "name": "knee joint",
                    "uri": "http://mouse.owl#MA_0000471",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hip joint",
                    "uri": "http://mouse.owl#MA_0000470",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "foot joint",
                    "uri": "http://mouse.owl#MA_0000464",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "ankle joint",
                    "uri": "http://mouse.owl#MA_0000463",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "forelimb joint",
                "uri": "http://mouse.owl#MA_0000614",
                "children": [
                  {
                    "depth": 4,
                    "name": "wrist joint",
                    "uri": "http://mouse.owl#MA_0000460",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "shoulder joint",
                    "uri": "http://mouse.owl#MA_0000459",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "hand joint",
                    "uri": "http://mouse.owl#MA_0000452",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "elbow joint",
                    "uri": "http://mouse.owl#MA_0000451",
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
        "children": [
          {
            "depth": 2,
            "name": "shoulder muscle",
            "uri": "http://mouse.owl#MA_0000633",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "arm skin",
            "uri": "http://mouse.owl#MA_0000596",
            "children": [
              {
                "depth": 3,
                "name": "elbow skin",
                "uri": "http://mouse.owl#MA_0000610",
                "children": []
              },
              {
                "depth": 3,
                "name": "upper arm skin",
                "uri": "http://mouse.owl#MA_0000608",
                "children": []
              },
              {
                "depth": 3,
                "name": "lower arm skin",
                "uri": "http://mouse.owl#MA_0000602",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "foot muscle",
            "uri": "http://mouse.owl#MA_0000652",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "knee joint",
            "uri": "http://mouse.owl#MA_0000471",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "blood",
            "uri": "http://mouse.owl#MA_0000059",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "nose",
            "uri": "http://mouse.owl#MA_0000281",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "foot joint",
            "uri": "http://mouse.owl#MA_0000464",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "trunk muscle",
            "uri": "http://mouse.owl#MA_0000514",
            "children": [
              {
                "depth": 3,
                "name": "chest muscle",
                "uri": "http://mouse.owl#MA_0000548",
                "children": []
              },
              {
                "depth": 3,
                "name": "pelvis muscle",
                "uri": "http://mouse.owl#MA_0000534",
                "children": []
              },
              {
                "depth": 3,
                "name": "abdomen muscle",
                "uri": "http://mouse.owl#MA_0000520",
                "children": []
              },
              {
                "depth": 3,
                "name": "back muscle",
                "uri": "http://mouse.owl#MA_0000496",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "elbow",
            "uri": "http://mouse.owl#MA_0000036",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "gastrointestinal system",
            "uri": "http://mouse.owl#MA_0000323",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "trunk skin",
            "uri": "http://mouse.owl#MA_0000517",
            "children": [
              {
                "depth": 3,
                "name": "thorax skin",
                "uri": "http://mouse.owl#MA_0000564",
                "children": [
                  {
                    "depth": 4,
                    "name": "chest skin",
                    "uri": "http://mouse.owl#MA_0000553",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "upper back skin",
                    "uri": "http://mouse.owl#MA_0000510",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "abdomen/pelvis/perineum skin",
                "uri": "http://mouse.owl#MA_0000530",
                "children": [
                  {
                    "depth": 4,
                    "name": "pelvis skin",
                    "uri": "http://mouse.owl#MA_0000546",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "abdomen skin",
                    "uri": "http://mouse.owl#MA_0000523",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "lower back skin",
                    "uri": "http://mouse.owl#MA_0000504",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "back skin",
                "uri": "http://mouse.owl#MA_0000498",
                "children": [
                  {
                    "depth": 4,
                    "name": "upper back skin",
                    "uri": "http://mouse.owl#MA_0000510",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "lower back skin",
                    "uri": "http://mouse.owl#MA_0000504",
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
        "children": [
          {
            "depth": 2,
            "name": "head/neck connective tissue",
            "uri": "http://mouse.owl#MA_0000570",
            "children": [
              {
                "depth": 3,
                "name": "neck connective tissue",
                "uri": "http://mouse.owl#MA_0000585",
                "children": [
                  {
                    "depth": 4,
                    "name": "neck cartilage",
                    "uri": "http://mouse.owl#MA_0000586",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "head connective tissue",
                "uri": "http://mouse.owl#MA_0000577",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "nerve",
            "uri": "http://mouse.owl#MA_0000217",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "lip skin",
            "uri": "http://mouse.owl#MA_0001579",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "lip",
            "uri": "http://mouse.owl#MA_0000343",
            "children": [
              {
                "depth": 3,
                "name": "lower lip",
                "uri": "http://mouse.owl#MA_0000921",
                "children": []
              },
              {
                "depth": 3,
                "name": "upper lip",
                "uri": "http://mouse.owl#MA_0000919",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "forebrain",
            "uri": "http://mouse.owl#MA_0000170",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "wrist joint",
            "uri": "http://mouse.owl#MA_0000460",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "iris nerve",
            "uri": "http://mouse.owl#MA_0001291",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "neck skin",
            "uri": "http://mouse.owl#MA_0000590",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "neck muscle",
            "uri": "http://mouse.owl#MA_0000587",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "pelvis muscle",
            "uri": "http://mouse.owl#MA_0000534",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "nose",
            "uri": "http://mouse.owl#MA_0000281",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "lip skeletal muscle",
            "uri": "http://mouse.owl#MA_0001578",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "eye muscle",
            "uri": "http://mouse.owl#MA_0000271",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "shoulder",
            "uri": "http://mouse.owl#MA_0000038",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "cornea",
            "uri": "http://mouse.owl#MA_0000266",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "oral region",
            "uri": "http://mouse.owl#MA_0000341",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hip skin",
            "uri": "http://mouse.owl#MA_0000658",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hand skin",
            "uri": "http://mouse.owl#MA_0000630",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "wrist",
            "uri": "http://mouse.owl#MA_0000039",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "forelimb joint",
            "uri": "http://mouse.owl#MA_0000614",
            "children": [
              {
                "depth": 3,
                "name": "wrist joint",
                "uri": "http://mouse.owl#MA_0000460",
                "children": []
              },
              {
                "depth": 3,
                "name": "shoulder joint",
                "uri": "http://mouse.owl#MA_0000459",
                "children": []
              },
              {
                "depth": 3,
                "name": "hand joint",
                "uri": "http://mouse.owl#MA_0000452",
                "children": []
              },
              {
                "depth": 3,
                "name": "elbow joint",
                "uri": "http://mouse.owl#MA_0000451",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head/neck skin",
            "uri": "http://mouse.owl#MA_0000574",
            "children": [
              {
                "depth": 3,
                "name": "neck skin",
                "uri": "http://mouse.owl#MA_0000590",
                "children": []
              },
              {
                "depth": 3,
                "name": "head skin",
                "uri": "http://mouse.owl#MA_0000582",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "shoulder joint",
            "uri": "http://mouse.owl#MA_0000459",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "forelimb skin",
            "uri": "http://mouse.owl#MA_0000617",
            "children": [
              {
                "depth": 3,
                "name": "wrist skin",
                "uri": "http://mouse.owl#MA_0000638",
                "children": []
              },
              {
                "depth": 3,
                "name": "shoulder skin",
                "uri": "http://mouse.owl#MA_0000635",
                "children": []
              },
              {
                "depth": 3,
                "name": "hand skin",
                "uri": "http://mouse.owl#MA_0000630",
                "children": []
              },
              {
                "depth": 3,
                "name": "arm skin",
                "uri": "http://mouse.owl#MA_0000596",
                "children": [
                  {
                    "depth": 4,
                    "name": "elbow skin",
                    "uri": "http://mouse.owl#MA_0000610",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "upper arm skin",
                    "uri": "http://mouse.owl#MA_0000608",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "lower arm skin",
                    "uri": "http://mouse.owl#MA_0000602",
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
        "children": [
          {
            "depth": 2,
            "name": "head organ",
            "uri": "http://mouse.owl#MA_0000581",
            "children": [
              {
                "depth": 3,
                "name": "muzzle/snout",
                "uri": "http://mouse.owl#MA_0001910",
                "children": []
              },
              {
                "depth": 3,
                "name": "jaw",
                "uri": "http://mouse.owl#MA_0001905",
                "children": []
              },
              {
                "depth": 3,
                "name": "nose",
                "uri": "http://mouse.owl#MA_0000281",
                "children": []
              },
              {
                "depth": 3,
                "name": "eye",
                "uri": "http://mouse.owl#MA_0000261",
                "children": []
              },
              {
                "depth": 3,
                "name": "ear",
                "uri": "http://mouse.owl#MA_0000236",
                "children": []
              },
              {
                "depth": 3,
                "name": "brain",
                "uri": "http://mouse.owl#MA_0000168",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "taste bud",
            "uri": "http://mouse.owl#MA_0001591",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "iris muscle",
            "uri": "http://mouse.owl#MA_0001287",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head skin",
            "uri": "http://mouse.owl#MA_0000582",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hand joint",
            "uri": "http://mouse.owl#MA_0000452",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "ear",
            "uri": "http://mouse.owl#MA_0000236",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "neck",
            "uri": "http://mouse.owl#MA_0000024",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "tongue",
            "uri": "http://mouse.owl#MA_0000347",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "iris blood vessel",
            "uri": "http://mouse.owl#MA_0001290",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "mouth",
            "uri": "http://mouse.owl#MA_0002474",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hindlimb joint",
            "uri": "http://mouse.owl#MA_0000662",
            "children": [
              {
                "depth": 3,
                "name": "knee joint",
                "uri": "http://mouse.owl#MA_0000471",
                "children": []
              },
              {
                "depth": 3,
                "name": "hip joint",
                "uri": "http://mouse.owl#MA_0000470",
                "children": []
              },
              {
                "depth": 3,
                "name": "foot joint",
                "uri": "http://mouse.owl#MA_0000464",
                "children": []
              },
              {
                "depth": 3,
                "name": "ankle joint",
                "uri": "http://mouse.owl#MA_0000463",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "arm muscle",
            "uri": "http://mouse.owl#MA_0000594",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "oral region",
            "uri": "http://mouse.owl#MA_0000341",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "oral region",
            "uri": "http://mouse.owl#MA_0000341",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "leg skin",
            "uri": "http://mouse.owl#MA_0000674",
            "children": [
              {
                "depth": 3,
                "name": "upper leg skin",
                "uri": "http://mouse.owl#MA_0000686",
                "children": []
              },
              {
                "depth": 3,
                "name": "lower leg skin",
                "uri": "http://mouse.owl#MA_0000680",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "brain white matter",
            "uri": "http://mouse.owl#MA_0000820",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hip joint",
            "uri": "http://mouse.owl#MA_0000470",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "knee",
            "uri": "http://mouse.owl#MA_0000046",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hindlimb muscle",
            "uri": "http://mouse.owl#MA_0000663",
            "children": [
              {
                "depth": 3,
                "name": "leg muscle",
                "uri": "http://mouse.owl#MA_0000672",
                "children": []
              },
              {
                "depth": 3,
                "name": "hip muscle",
                "uri": "http://mouse.owl#MA_0000657",
                "children": []
              },
              {
                "depth": 3,
                "name": "foot muscle",
                "uri": "http://mouse.owl#MA_0000652",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "sensory organ",
            "uri": "http://mouse.owl#MA_0000017",
            "children": [
              {
                "depth": 3,
                "name": "tongue",
                "uri": "http://mouse.owl#MA_0000347",
                "children": []
              },
              {
                "depth": 3,
                "name": "nose",
                "uri": "http://mouse.owl#MA_0000281",
                "children": []
              },
              {
                "depth": 3,
                "name": "eye",
                "uri": "http://mouse.owl#MA_0000261",
                "children": []
              },
              {
                "depth": 3,
                "name": "ear",
                "uri": "http://mouse.owl#MA_0000236",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "outer ear",
            "uri": "http://mouse.owl#MA_0000258",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "foot",
            "uri": "http://mouse.owl#MA_0000044",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "forelimb muscle",
            "uri": "http://mouse.owl#MA_0000615",
            "children": [
              {
                "depth": 3,
                "name": "shoulder muscle",
                "uri": "http://mouse.owl#MA_0000633",
                "children": []
              },
              {
                "depth": 3,
                "name": "hand muscle",
                "uri": "http://mouse.owl#MA_0000628",
                "children": []
              },
              {
                "depth": 3,
                "name": "arm muscle",
                "uri": "http://mouse.owl#MA_0000594",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "palate",
            "uri": "http://mouse.owl#MA_0002476",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "muzzle/snout",
            "uri": "http://mouse.owl#MA_0001910",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head/neck muscle",
            "uri": "http://mouse.owl#MA_0000571",
            "children": [
              {
                "depth": 3,
                "name": "neck muscle",
                "uri": "http://mouse.owl#MA_0000587",
                "children": []
              },
              {
                "depth": 3,
                "name": "head muscle",
                "uri": "http://mouse.owl#MA_0000578",
                "children": [
                  {
                    "depth": 4,
                    "name": "cranial/facial muscle",
                    "uri": "http://mouse.owl#MA_0000579",
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
        "children": [
          {
            "depth": 2,
            "name": "autopod",
            "uri": "http://mouse.owl#MA_0002714",
            "children": [
              {
                "depth": 3,
                "name": "foot",
                "uri": "http://mouse.owl#MA_0000044",
                "children": []
              },
              {
                "depth": 3,
                "name": "hand",
                "uri": "http://mouse.owl#MA_0000037",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "foot skin",
            "uri": "http://mouse.owl#MA_0000654",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head muscle",
            "uri": "http://mouse.owl#MA_0000578",
            "children": [
              {
                "depth": 3,
                "name": "cranial/facial muscle",
                "uri": "http://mouse.owl#MA_0000579",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "knee skin",
            "uri": "http://mouse.owl#MA_0000668",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "wrist skin",
            "uri": "http://mouse.owl#MA_0000638",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "retina",
            "uri": "http://mouse.owl#MA_0000276",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "eye surface",
            "uri": "http://mouse.owl#MA_0002486",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "eye",
            "uri": "http://mouse.owl#MA_0000261",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head connective tissue",
            "uri": "http://mouse.owl#MA_0000577",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "head/neck organ",
            "uri": "http://mouse.owl#MA_0000573",
            "children": [
              {
                "depth": 3,
                "name": "neck organ",
                "uri": "http://mouse.owl#MA_0000589",
                "children": []
              },
              {
                "depth": 3,
                "name": "head organ",
                "uri": "http://mouse.owl#MA_0000581",
                "children": [
                  {
                    "depth": 4,
                    "name": "muzzle/snout",
                    "uri": "http://mouse.owl#MA_0001910",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "jaw",
                    "uri": "http://mouse.owl#MA_0001905",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "nose",
                    "uri": "http://mouse.owl#MA_0000281",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "eye",
                    "uri": "http://mouse.owl#MA_0000261",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "ear",
                    "uri": "http://mouse.owl#MA_0000236",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "brain",
                    "uri": "http://mouse.owl#MA_0000168",
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
        "children": [
          {
            "depth": 2,
            "name": "face",
            "uri": "http://mouse.owl#MA_0002473",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "heart",
            "uri": "http://mouse.owl#MA_0000072",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "pupil",
            "uri": "http://mouse.owl#MA_0001292",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "shoulder skin",
            "uri": "http://mouse.owl#MA_0000635",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "elbow joint",
            "uri": "http://mouse.owl#MA_0000451",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hand muscle",
            "uri": "http://mouse.owl#MA_0000628",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hand",
            "uri": "http://mouse.owl#MA_0000037",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "cheek",
            "uri": "http://mouse.owl#MA_0002475",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "hindlimb skin",
            "uri": "http://mouse.owl#MA_0000665",
            "children": [
              {
                "depth": 3,
                "name": "leg skin",
                "uri": "http://mouse.owl#MA_0000674",
                "children": [
                  {
                    "depth": 4,
                    "name": "upper leg skin",
                    "uri": "http://mouse.owl#MA_0000686",
                    "children": []
                  },
                  {
                    "depth": 4,
                    "name": "lower leg skin",
                    "uri": "http://mouse.owl#MA_0000680",
                    "children": []
                  }
                ]
              },
              {
                "depth": 3,
                "name": "knee skin",
                "uri": "http://mouse.owl#MA_0000668",
                "children": []
              },
              {
                "depth": 3,
                "name": "hip skin",
                "uri": "http://mouse.owl#MA_0000658",
                "children": []
              },
              {
                "depth": 3,
                "name": "foot skin",
                "uri": "http://mouse.owl#MA_0000654",
                "children": []
              },
              {
                "depth": 3,
                "name": "ankle skin",
                "uri": "http://mouse.owl#MA_0000641",
                "children": []
              }
            ]
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "muscle tissue",
            "uri": "http://mouse.owl#MA_0002437",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "pelvis skin",
            "uri": "http://mouse.owl#MA_0000546",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "inner ear",
            "uri": "http://mouse.owl#MA_0000237",
            "children": []
          }
        ]
      },
      {
        "depth": 1,
        "children": [
          {
            "depth": 2,
            "name": "leg muscle",
            "uri": "http://mouse.owl#MA_0000672",
            "children": []
          }
        ]
      }
    ]
  }
};

mapping_human_mouse = {
  "onto2": "http://mouse.owl",
  "onto1": "http://human.owl",
  "alignments": [
      {
          "entity2": "eyelash",
          "entity1": "Eyelash"
      },
      {
          "entity2": "cranium",
          "entity1": "Skull"
      },
      {
          "entity2": "skin",
          "entity1": "Skin"
      },
      {
          "entity2": "visceral organ system",
          "entity1": "Viscera"
      },
      {
          "entity2": "muscle",
          "entity1": "Muscle"
      },
      {
          "entity2": "cartilage",
          "entity1": "Cartilage"
      },
      {
          "entity2": "body fluid/substance",
          "entity1": "Body_Fluid_or_Substance"
      },
      {
          "entity2": "nasal mucus",
          "entity1": "Nasal_Mucus"
      },
      {
          "entity2": "joint",
          "entity1": "Joint"
      },
      {
          "entity2": "blood",
          "entity1": "Blood"
      }
  ]
};
