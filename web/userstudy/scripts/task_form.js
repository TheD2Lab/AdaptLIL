const DomainCode = {
    CONFERENCE: 0,
    ANATOMY: 1
};

//var selectedDomain = DomainCode.CONFERENCE;
var taskset;
var tasknum=0;
var csvData;
var timeData;

window.addEventListener('load', function(event) {
    console.log("Task: Ready to generate form!");

    csvData = new Array(30);
    timeData = new Array(30);
    startTime = new Date().getTime();
    this.selectedDomain = getDomain();
    initCSV(csvData);
    
    //Data
    taskset = getTaskset(selectedDomain);
    if(selectedDomain==0) 
    {
        setDataset(dataset1);
    } else {
        setDataset(dataset2);
    }

    selectedTask = assignOneTask(taskset, tasknum);
    generateTaskForm(selectedTask);
    tasknum++;
});

function initCSV(csvData) {
    this.csvData = csvData;
}

function setTask(task) {
    this.selectedTask = task;
}

function setDomain(domain) {
    this.selectedDomain = domain;
}

function getDomain() {
  var queryString = window.location.search;
  var urlParams = new URLSearchParams(queryString);
  var domain = urlParams.get('domain');
  if(domain=='Conference') return 0;
  else if(domain=='Anatomy') return 1;
}

function setVis(vis) {
    this.vis = vis;
}

function getParticipant() {
  var queryString = window.location.search;
  var urlParams = new URLSearchParams(queryString);
  return urlParams.get('participantID');
}

function getTaskset(selectedDomain) {
    switch(selectedDomain) {
        case DomainCode.CONFERENCE:
            taskset = taskDatasets.conference;
            break;
        case DomainCode.ANATOMY:
            taskset = taskDatasets.anatomy;
            break;
    }
    return taskset;
}

function assignOneTask(taskset, n) {
    //console.log(`Task: '${taskset.domain}' domain selected as a task dataset.`);
    return taskset.tasks[n];
}

function nextTask() {
    var valid = validateForm();
    if(valid) {
        // task submission timestamp
        timeData[tasknum] = new Date().getTime() - startTime;
        console.log("Task", tasknum, "submission timestamp:",timeData[tasknum])
        saveData();
        if(tasknum==taskset.tasks.length) {
            // downloading data
            window.open('data:text/csv;charset=utf-8' + csvData.join(','));
            document.getElementById("submit").type="submit";
            document.getElementById("taskForm").action = "closing_form.html";
            return valid;
        } else if(tasknum == taskset.tasks.length-1) {
            document.getElementById("submit").value="Finish";
        }
        selectedTask = assignOneTask(taskset, tasknum++);
        generateTaskForm(selectedTask);
        return valid;
    } else {
        nextTask();
    }
}

function generateTaskForm(task) {
    console.log('Task: generateTaskForm()');
    console.log(`Task: qtype:${task.qtype} atype:${task.atype}`);
    // console.log(task);

    //Show question
    $('#taskDiv .task-question').html(task.question);

    //Show answer input
    //3 types of answer: 1) y/n, 2) number, 3) class
    //var answerDiv = $('#taskDiv #answerDiv');
    var answerDiv = document.getElementById("answerDiv");
    if (task.atype == "y/n") {
        answerDiv.innerHTML=(`
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
        answerDiv.innerHTML=(`
            <select class="task-number-answer form-control" id="inputNumber" name="taskNumber" onkeydown="return (event.keyCode!=13);">
                <option selected>Choose...</option>
                <option value="${task.options[0]}">${task.options[0]}</option>
                <option value="${task.options[1]}">${task.options[1]}</option>
                <option value="${task.options[2]}">${task.options[2]}</option>
                <option value="${task.options[3]}">${task.options[3]}</option>
            </select>
        `)
    } else if (task.atype == "class") {
        answerDiv.innerHTML=(`
        <input type=text id="inputText" name="taskClassName" class="form-control" onkeydown="return (event.keyCode!=13);" autocomplete="off">
    `);
    } else if(task.atype == "pairs") {
        answerDiv.innerHTML=(`
            <input type=text id="inputText" name="taskClassName" class="form-control" style="width:200px" onkeydown="return (event.keyCode!=13);" autocomplete="off">
        `);
    }
    
}

/**
 * Validates task form
 */
function validateForm() {
    //Validates based on input form
    if (selectedTask.atype == "y/n") {
        var select = $('select.task-answer').val();
        console.log('Task: select:', select);
        if (select != "yes" && select != "no") {
            /* $('#validateMsg').html("Complete the task.");
            return false; */
        }
    } else if (selectedTask.atype == "number") {
        /* if ($('#answerDiv #inputNumber').val().length == 0) {
            $('#validateMsg').html("Complete the task.");
            return false;
        }
        if ($('#answerDiv #inputNumber').val() < 0) {
            $('#validateMsg').html("Answer cannot be negative.");
            return false;
        } */
    } else if (selectedTask.atype == "class") {
        /* if ($('#answerDiv #inputText').val().length < 3) {
            $('#validateMsg').html("Complete the task.");
            return false;
        }*/ 
    }
    //TODO: validate other condition like 1) some interaction with the visualization..
    $('#validateMsg').html("");
    return true;
}

function saveData() {
    let c = tasknum;
    this.csvData[c] = new Array();
    // visualization
    this.csvData[c][0] = this.vis;
    // domain
    this.csvData[c][1] = this.selectedDomain;
    // qtype
    this.csvData[c][2] = this.selectedTask.qtype;
    // question
    this.csvData[c][3] = this.selectedTask.question.replaceAll(",",""); // removing commas for later join
    // user answer
    var text_input;
    switch(selectedTask.atype) {
        case "number":
            //let num_input = $('#answerDiv #inputNumber').val();
            let num_input = $('select.task-number-answer').val();
            // checking for null values
            if(num_input=="Choose...") {
                this.csvData[c][4] = ""; // no selection
            } else {
                this.csvData[c][4] = num_input;
            }
            // checking for correct answer
            if(this.csvData[c][4] == this.selectedTask.answer) {
                this.csvData[c][5] = 1;
            } else {
                this.csvData[c][5] = 0;
            }
            break;
        case "class":
            text_input = $('#answerDiv #inputText').val();
            if(text_input==null) {
                this.csvData[c][4] = "";
            } else {
                this.csvData[c][4] = text_input;
            }
            // correct
            if(this.csvData[c][4].toLowerCase() == this.selectedTask.answer.toLowerCase()) {
                this.csvData[c][5] = 1;
            } else {
                this.csvData[c][5] = 0;
            }
            break;
        case "y/n":
            let menu_input = $('select.task-answer').val();
            if(menu_input=="Choose...") {
                this.csvData[c][4] = ""; // no selection
            } else {
                this.csvData[c][4] = menu_input;
            }
            // correct
            if(this.csvData[c][4].toLowerCase() == this.selectedTask.answer.toLowerCase()) {
                this.csvData[c][5] = 1;
            } else {
                this.csvData[c][5] = 0;
            }
            break;
        case "pairs":
            text_input = $('#answerDiv #inputText').val();
            text_input = text_input.replace('\s/g', ''); // removing spaces
            if(text_input==null) {
                this.csvData[c][4] = ""; // no selection
            } else {
                this.csvData[c][4] = text_input;
            }
            let input_arr = text_input.split(",");
            for (var i=0; i<input_arr.length; i++) { // checking answer
                if(this.selectedTask.answer.includes(input_arr[i])) {
                    this.csvData[c][5] = 1;
                    break;
                } else {
                    this.csvData[c][5] = 0;
                }
            }
            break;
    }
    // p_id
    this.csvData[c][6] = this.participant;
    // timestamp
    this.csvData[c][7] = timeData[c]; // end

    // window.open('data:text/csv;charset=utf-8' + csvData.join(','));
    this.csvData[c].join(',');
    this.csvData[c] += "\n";

    console.log('Data added successfully');
}
