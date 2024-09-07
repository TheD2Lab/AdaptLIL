const FormCode = {
    WORKLOAD: 0,
    USABILITY: 1,
    REACTION: 2
};

var selectedForm;

window.addEventListener('load', function() {
    console.log("window loaded.");

    var data = formdata.reaction; //formdata.tlx, formdata.sus, formdata.reaction
    selectedForm = data;
    generateForm(data);
});

/**
 * Generate feedback form
 * @param {*} data form data object
 */
function generateForm(data) {
    console.log('generating form...');

    //Show form description
    $("#form-title").text(data.title);
    $("#form-message").text(data.message);

    //Add hidden id/formtype info for form submit
    var pid = "E9R7A001"; //TODO: some 8-digit random generated id
    $('#form-identification').append(`<input type="text" id="pId" name="pId" value="${pid}" readonly>`);
    $('#form-identification').append(`<input type="text" id="formtype" name="type" value="${data.type}" readonly>`);
    
    //Draw the form on html page
    console.log('formtype: ' + data.formcode);
    switch(data.formcode) {
        case FormCode.WORKLOAD:
        case FormCode.USABILITY:
            drawScaleForm(data);
            break;
        case FormCode.REACTION:
            drawReactionForm(data);
            break;
        default:
            console.log("invalid formtype is selected.");
    }
}

/**
 * Draw scale based questionnaire type of form to html.
 * @param {*} data. either workload TLX or usability SUS data
 */
function drawScaleForm(data) {
    console.log("drawScaleForm()");
    if (data.itmes == "undefined") return;
    
    data.items.forEach((item,i) => {
        idx = i+1;
        //question item
        var itemDiv = $('<div/>', {
            id: 'item' + idx,
            class: 'itemdiv'
        });
        //question text
        text = idx + ". " + item.question;
        itemDiv.append($('<p/>', {class: 'qtext', html: text}));
        //question subtext
        if (item.subtext != "undefined") {
            itemDiv.append($('<p/>', {class: 'qtext-sub', html: item.subtext}));
        }
        //radio group
        itemDiv.append(createRadioGroup("a"+idx, data.scale));

        $('#form-content').append(itemDiv);
    });
}

/**
 * Create a radio group
 * @param {string} inputName name of the radio group
 * @param {object} scale {count, fisrt, last}
 */
function createRadioGroup(inputName, scale) {
    var answerDiv = $('<div class="answerdiv text-center">');
    answerDiv.append($('<label>'+ scale.first +'</label>'));    //label for the smallest option
    var radiogroup = $('<div class="form-check form-check-inline" role="radiogroup">');
    for (var i=1; i<=scale.count; i++) {
        var radio = $('<div class="radiodiv"></div>');
        radio.append($(`<label class="form-check-label" for="inlineRadio${i}">${i}</label>`));
        radio.append($('<input/>', {
            class: "form-check-input",
            type: "radio",
            name: inputName,
            id: "inlineRadio" + i,
            value: i,
            required: "required"
        }));
        radiogroup.append(radio);
    }
    answerDiv.append(radiogroup);
    answerDiv.append($('<label>'+ scale.last +'</label>'));    //label for the largest option
    return answerDiv;
}

/**
 * Draws Reaction Cards form
 * @param {*} data 
 */
function drawReactionForm(data) {
    //const limit = 5;

    //Draw reaction cards
    var cardsDiv = $('<div class="cards-div"></div>');
    shuffle(data.items);
    for (var word of data.items) {
        var wordcard = $(`
            <label class="btn wordcard">
                ${word}
                <input type="checkbox" class="word-checkbox" value="${word}" autocomplete="off">
            </label>
        `);
        cardsDiv.append(wordcard);
    }
    $('#form-content').append(cardsDiv);

    //Chosen words
    var chosenCardsDiv = $('<div class="chosen-cards-div"></div>');
    chosenCardsDiv.append($('<input id="chosen-words" type="text" name="words" value="" readonly>'));
    $('#form-content').append(chosenCardsDiv);

    //Checkbox interaction
    $('input.word-checkbox').on('change', function() {
        var checkbox = $(this);
        var label = checkbox.parent('label');
        
        //Set limit
        /* if ($('input.word-checkbox:checked').length > limit) {
            $(this).prop('checked', false);
            //console.log('word selection limit 5');
            return;
        }*/
        
        //Sync with button
        if (checkbox.is(':checked')) {
            label.addClass('active');
        } else {
            label.removeClass('active');
        }
        
        //list of words
        var checkedboxes = $('input.word-checkbox:checked').toArray();
        var words = checkedboxes.map(c => c.value);
        $('#chosen-words').val(words.join(', '));
    });

    //Creating text input for participant to enter their ID
    /* 
    var labelDiv = $(`
        <div class="label-div form-group">
            <label for="formGroupExampleInput">${data.q2}</label>
            <textarea class="form-control" id="pTextarea" rows="1" name="id" required></textarea>
        </div>`);
    $('#form-content').append(labelDiv); */
}

function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
      let j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
}

function saveResponse() {
    var csvData = new Array();
    let c = 1;
    var checkedboxes = $('input.word-checkbox:checked').toArray();
    var words = checkedboxes.map(c => c.value);
    console.log(words);
    for (let i=0; i<words.length; i++) {
        csvData[c] = words.at(i);
        c++;
    }
    var link = document.createElement("a");
    link.setAttribute("href", 'data:text/csv;charset=utf-8' + csvData.join(','));
    link.setAttribute("download", "reaction_data.csv");
    document.body.appendChild(link); // Required for FF

    link.click(); // This will download the data file named "my_data.csv".
    //window.close();
}

function closeWindow() {
    window.close();
}

function validateForm() {
    /* switch(selectedForm.formcode) {
        case FormCode.REACTION:
            //Should select 5 words
            if ($('input.word-checkbox:checked').length < 5) {
                $('#validateMsg').html("Select 5 words");
                return false;
            } 
            //Should fill out id
            if ($('#pTextarea').val().length == 0) {
                $('#validateMsg').html("Please provide your ID");
                return false;
            }
            //TODO: Validate the reason textarea.. if
            // 1) only symbols typed, 2) same character repeated
            break;
        case FormCode.WORKLOAD:
        case FormCode.WORKLOAD:
            //TODO: validate the scale form
            break;
            
    }*/
    return true;
}