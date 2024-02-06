const DomainCode = {
    CONFERENCE: 0,
    ANATOMY: 1
};

class TaskForm {
    constructor(ontologyMap) {
        this.ontologyMap = ontologyMap;
    }

    initOnLoad() {
        this.csvData = new Array(30);
        this.timeData = new Array(30);
        this.startTime = new Date().getTime();
        this.selectedDomain = this.getDomain();
        this.initCSV(this.csvData);
        this.tasknum = 0;

        //Data
        this.taskset = this.getTaskset(this.selectedDomain);
        if (this.selectedDomain == 0) {
            this.ontologyMap.setDataset(dataset1);
        } else {
            this.ontologyMap.setDataset(dataset2);
        }

        this.selectedTask = this.assignOneTask(this.taskset, this.tasknum);
        this.generateTaskForm(this.selectedTask);
        this.tasknum++;
    }


    initCSV(csvData) {
        this.csvData = csvData;
    }


    setTask(task) {
        this.selectedTask = task;
    }


    setDomain(domain) {
        this.selectedDomain = domain;
    }

    setWebsocket(websocket) {
        this.websocket = websocket;
    }


    getDomain() {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const domain = urlParams.get('domain');
        if (domain == 'Conference') return 0;
        else if (domain == 'Anatomy') return 1;
    }


    setVis(vis) {
        this.vis = vis;
    }


    getParticipant() {
        var queryString = window.location.search;
        var urlParams = new URLSearchParams(queryString);
        return urlParams.get('participantID');
    }


    getTaskset(selectedDomain) {
        switch (selectedDomain) {
            case DomainCode.CONFERENCE:
                this.taskset = taskDatasets.conference;
                break;
            case DomainCode.ANATOMY:
                this.taskset = taskDatasets.anatomy;
                break;
        }
        return this.taskset;
    }


    assignOneTask(taskset, n) {
        //console.log(`Task: '${taskset.domain}' domain selected as a task dataset.`);
        console.log(taskset);
        console.log(taskset.tasks);
        console.log(n);
        return taskset.tasks[n];
    }


    nextTask() {
        const valid = this.validateForm();
        if (valid) {
            // task submission timestamp
            this.timeData[this.tasknum] = new Date().getTime() - this.startTime;
            console.log("Task", this.tasknum, "submission timestamp:", this.timeData[this.tasknum])
            const queryString = window.location.search;
            const urlParams = new URLSearchParams(queryString);
            const is_adaptive = urlParams.get('is-adaptive');
            const pid = urlParams.get("participantID")
            const domain = urlParams.get("domain");
            const file_name = pid + "_"+domain+"_"+is_adaptive+"_answers.csv";
            this.saveData();
            if (this.tasknum == this.taskset.tasks.length) {
                // downloading datavar encodedUri =csvContent);
                var link = document.createElement("a");
                link.setAttribute("href",  encodeURI('data:text/csv;charset=utf-8' + this.csvData.join(',')));
                link.setAttribute("download", file_name);
                document.body.appendChild(link); // Required for FF

                link.click(); // This will download the data file named "my_data.csv".
                // window.open('data:text/csv;charset=utf-8' + this.websocket.adaptationData(e => e.join(",")).join('\n'))
                document.getElementById("submit").type = "submit";
                document.getElementById("taskForm").action = "closing_form.html";
                return valid;
            } else if (this.tasknum == this.taskset.tasks.length - 1) {
                document.getElementById("submit").value = "Finish";
            }
            this.selectedTask = this.assignOneTask(this.taskset, this.tasknum++);
            this.generateTaskForm(this.selectedTask);
            return valid;
        } else {
            this.nextTask();
        }
    }


    generateTaskForm(task) {
        console.log('Task: generateTaskForm()');
        console.log(`Task: qtype:${task.qtype} atype:${task.atype}`);
        // console.log(task);

        //Show question
        $('#taskDiv .task-question').html(task.question);

        //Show answer input
        //3 types of answer: 1) y/n, 2) number, 3) class
        //var answerDiv = $('#taskDiv #answerDiv');
        const answerDiv = document.getElementById("answerDiv");
        if (task.atype == "y/n") {
            answerDiv.innerHTML = (`
            <select class="task-answer form-control" id="inputSelect" name="taskSelect" onkeydown="return (event.keyCode!=13);">
                <option selected>Choose...</option>
                <option value="yes">Yes</option>
                <option value="no">No</option>
            </select>
        `);
        } else if (task.atype == "number") {
            /* answerDiv.innerHTML=(`
                <input type=number id="inputNumber" name="taskNumber" class="form-control" min="0" onkeydown="return (event.keyCode!=13 && event.keyCode!=189);">
            `); */
            answerDiv.innerHTML = (`
            <select class="task-number-answer form-control" id="inputNumber" name="taskNumber" onkeydown="return (event.keyCode!=13);">
                <option selected>Choose...</option>
                <option value="${task.options[0]}">${task.options[0]}</option>
                <option value="${task.options[1]}">${task.options[1]}</option>
                <option value="${task.options[2]}">${task.options[2]}</option>
                <option value="${task.options[3]}">${task.options[3]}</option>
            </select>
        `)
        } else if (task.atype == "class") {
            answerDiv.innerHTML = (`
        <input type=text id="inputText" name="taskClassName" class="form-control" onkeydown="return (event.keyCode!=13);" autocomplete="off">
    `);
        } else if (task.atype == "pairs") {
            answerDiv.innerHTML = (`
            <input type=text id="inputText" name="taskClassName" class="form-control" style="width:200px" onkeydown="return (event.keyCode!=13);" autocomplete="off">
        `);
        }

    }

    /**
     * Validates task form
     */

    validateForm() {
        //Validates based on input form
        if (this.selectedTask.atype == "y/n") {
            const select = $('select.task-answer').val();
            console.log('Task: select:', select);
            if (select != "yes" && select != "no") {
                /* $('#validateMsg').html("Complete the task.");
                return false; */
            }
        } else if (this.selectedTask.atype == "number") {
            /* if ($('#answerDiv #inputNumber').val().length == 0) {
                $('#validateMsg').html("Complete the task.");
                return false;
            }
            if ($('#answerDiv #inputNumber').val() < 0) {
                $('#validateMsg').html("Answer cannot be negative.");
                return false;
            } */
        } else if (this.selectedTask.atype == "class") {
            /* if ($('#answerDiv #inputText').val().length < 3) {
                $('#validateMsg').html("Complete the task.");
                return false;
            }*/
        }
        //TODO: validate other condition like 1) some interaction with the visualization..
        $('#validateMsg').html("");
        return true;
    }


    saveData() {
        let c = this.tasknum;
        this.csvData[c] = new Array();
        // visualization
        this.csvData[c][0] = this.vis;
        // domain
        this.csvData[c][1] = this.selectedDomain;
        // qtype
        this.csvData[c][2] = this.selectedTask.qtype;
        // question
        this.csvData[c][3] = this.selectedTask.question.replaceAll(",", ""); // removing commas for later join
        // user answer
        var text_input;
        switch (this.selectedTask.atype) {
            case "number":
                //let num_input = $('#answerDiv #inputNumber').val();
                let num_input = $('select.task-number-answer').val();
                // checking for null values
                if (num_input == "Choose...") {
                    this.csvData[c][4] = ""; // no selection
                } else {
                    this.csvData[c][4] = num_input;
                }
                // checking for correct answer
                if (this.csvData[c][4] == this.selectedTask.answer) {
                    this.csvData[c][5] = 1;
                } else {
                    this.csvData[c][5] = 0;
                }
                break;
            case "class":
                text_input = $('#answerDiv #inputText').val();
                if (text_input == null) {
                    this.csvData[c][4] = "";
                } else {
                    this.csvData[c][4] = text_input;
                }
                // correct
                if (this.csvData[c][4].toLowerCase() == this.selectedTask.answer.toLowerCase()) {
                    this.csvData[c][5] = 1;
                } else {
                    this.csvData[c][5] = 0;
                }
                break;
            case "y/n":
                let menu_input = $('select.task-answer').val();
                if (menu_input == "Choose...") {
                    this.csvData[c][4] = ""; // no selection
                } else {
                    this.csvData[c][4] = menu_input;
                }
                // correct
                if (this.csvData[c][4].toLowerCase() == this.selectedTask.answer.toLowerCase()) {
                    this.csvData[c][5] = 1;
                } else {
                    this.csvData[c][5] = 0;
                }
                break;
            case "pairs":
                text_input = $('#answerDiv #inputText').val();
                text_input = text_input.replace('\s/g', ''); // removing spaces
                if (text_input == null) {
                    this.csvData[c][4] = ""; // no selection
                } else {
                    this.csvData[c][4] = text_input;
                }
                let input_arr = text_input.split(",");
                for (var i = 0; i < input_arr.length; i++) { // checking answer
                    if (this.selectedTask.answer.includes(input_arr[i])) {
                        this.csvData[c][5] = 1;
                        break;
                    } else {
                        this.csvData[c][5] = 0;
                    }
                }
                break;
        }
        // p_id
        this.csvData[c][6] = this.getParticipant();
        // timestamp
        this.csvData[c][7] = this.timeData[c]; // end

        // window.open('data:text/csv;charset=utf-8' + csvData.join(','));
        this.csvData[c].join(',');
        this.csvData[c] += "\n";

        console.log('Data added successfully');


    }
}