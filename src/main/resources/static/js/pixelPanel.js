let socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log(frame);
    stompClient.subscribe('/pixel/editor', function () {
        redraw(fetchMatrix())
    });
});


let WIDTH = 160;
let HEIGHT = 95;
setTableSize()

function redraw(colorMatrix) {
    let newRow = document.createElement('TR')
    let newColumn = document.createElement('TD')
    $("#table-of-pixel").empty()
    for (let index = 0; index < HEIGHT; index += 1) {
        newRow = document.createElement('TR')
        document.querySelector('#table-of-pixel').appendChild(newRow)
        createLineOfTable(WIDTH, index, colorMatrix)
    }
}
function createLineOfTable(size, rowIndex, colorMatrix) {
    let ops = document.querySelector('#table-of-pixel').lastChild
    for (let index = 0; index < size; index += 1) {
        newColumn = document.createElement('TD')
        newColumn.id = rowIndex + ":" + index;
        newColumn.style.backgroundColor = colorMatrix[rowIndex][index];

        ops.appendChild(newColumn).className = 'pixel'


    }
}

function fetchMatrix() {
    let data;
    $.ajax({
        url: "/twitch-bot/pixel/matrix",
        dataType: 'json',
        type: 'GET',
        contentType: "application/json",
        async: false,
        success: function (response) {
            data = response;
        }
    })
    let matrix = Array(HEIGHT).fill("white").map(() => Array(WIDTH).fill("white"))
    for (let position in data) {
        let row = position.split(":")[0];
        let col = position.split(":")[1];

        if (row < HEIGHT && col < WIDTH) {
            matrix[row][col] = data[position]
        }
    }
    return matrix;
}

function setTableSize() {
    $.ajax({
        method: "GET",
        url: "/twitch-bot/size",
        async: false,
        success: function (data) {
            WIDTH = data.width
            HEIGHT = data.height
        }
    })
}

