const formdata = {
    "tlx": {
        "formcode": 0,
        "type": "tlx",
        "title": "Workload",
        "desc": "NASA-TLX Workload",
        "message": "Please select a point on the scale of 1 to 5 that best describes your experience during the task you've just completed. There is no right or wrong answer.",
        "scale": {
            "count": 5,
            "first": "very low",
            "last": "very high"
        },
        "items": [
            {
                "question": "How mentally demanding was the task?",
                "subtext": "In other words, how much mental and perceptual activity was required (e.g. thinking, deciding, calculating, remembering, looking, searching etc.)? Was the task easy or demanding, simple or complex, exacting or forgiving?"
            },{
                "question": "How physically demanding was the task?",
                "subtext": "In other words, how much physical activity was required (e.g. pushing, pulling, turning, controlling, activating etc.)? Was the task easy or demanding, slow or brisk, slack or strenuous, restful or laborious?"
            },{
                "question": "How hurried or rushed was the pace of the task?",
                "subtext": "In other words, how much time pressure did you feel due to the rate of pace at which the tasks or task elements occurred? Was the pace slow and leisurely or rapid and frantic?"
            },{
                "question": "How successful were you in accomplishing what you were asked to do?",
                "subtext": "In other words, how successful do you think you were in accomplishing the goals of the task set by the researcher? How satisfied were you with your performance in accomplishing these goals?"
            },{
                "question": "How hard did you have to work to accomplish your level of performance?",
                "subtext": "In other words, how hard did you have to work (mentally and physically) to accomplish your level of performance?"
            },{
                "question": "How insecure, discouraged, irritated, stressed, and annoyed were you?",
                "subtext": "How insecure, discouraged, irritated, stressed and annoyed versus secure, gratified, content, relaxed and complacent did you feel during the task?"
            }
        ]
    },
    "sus": {
        "formcode": 1,
        "type": "sus",
        "title": "Usability",
        "desc": "System Usability Scale(SUS)",
        "message": "Please select a point on the scale of 1 to 5 that best describes how much you agree/disagree with each statement below about the visualization you've just used to complete the task. There is no right or wrong answer.",
        "scale": {
            "count": 5,
            "first": "strongly disagree",
            "last": "strongly agree"
        },
        "items": [
            {
                "question": "I think that I would like to use this visualization frequently (when completing similar tasks).",
            },{
                "question": "I found the visualization unnecessarily complex.",
            },{
                "question": "I thought the visualization was easy to use.",
            },{
                "question": "I think I would need the support of a technical person to be able to use this visualization.",
            },{
                "question": "I found the various functions in this visualization were well integrated.",
            },{
                "question": "I thought this visualization was too inconsistent.",
            },{
                "question": "I would imagine that most people would learn to use this visualization very quickly.",
            },{
                "question": "I found the visualization very cumbersome to use.",
            },{
                "question": "I felt very confident using the visualization.",
            },{
                "question": "I needed to learn a lot of things before I could get going with this visualization."
            }
        ]
    },
    "reaction": {
        "formcode": 2,
        "type": "reaction",
        "title": "Reaction Cards",
        "desc": "Microsoft Product Desirability Reaction Cards (64 out of 118 words)",
        "message": "Please select which words best describe your opinion on the visualization you've just used to complete the task. There is no right or wrong answer. You can select as many words as you want.",
        "items": [
            "Advanced",
            "Annoying",
            "Approachable",
            "Attractive",
            "Busy",
            "Clean",
            "Collaborative",
            "Comfortable",
            "Complex",
            "Comprehensive",
            "Confusing",
            "Consistent",
            "Convenient",
            "Creative",
            "Cutting edge",
            "Dated",
            "Desirable",
            "Difficult",
            "Dull",
            "Easy to use",
            "Effective",
            "Efficient",
            "Engaging",
            "Entertaining",
            "Essential",
            "Exceptional",
            "Exciting",
            "Familiar",
            "Fast",
            "Flexible",
            "Friendly",
            "Helpful",
            "High quality",
            "Impersonal",
            "Inconsistent",
            "Ineffective",
            "Innovative",
            "Inspiring",
            "Intimidating",
            "Inviting",
            "Irrelevant",
            "Organized",
            "Overwhelming",
            "Patronizing",
            "Personal",
            "Poor quality",
            "Powerful",
            "Predictable",
            "Professional",
            "Relevant",
            "Reliable",
            "Rigid",
            "Satisfying",
            "Simplistic",
            "Straight Forward",
            "Stressful",
            "Time-consuming",
            "Trustworthy",
            "Unattractive",
            "Unconventional",
            "Undesirable",
            "Unpredictable",
            "Unrefined",
            "Useful"
        ],
        //"q2": "Please enter your participant ID below, then hit \"Submit\"."
    }
};