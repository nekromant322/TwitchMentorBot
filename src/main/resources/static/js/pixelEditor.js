let accessToken = document.location.hash.split("&")[0].split("=")[1]
let countPixels = 0

let WIDTH = 160;
let HEIGHT = 95;
displayPixelsAvailable()
setTableSize()
document.querySelector('#black').className = "color selected"

let stompClient = null;

let socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log(frame);
});


let colorsFromPalette = document.getElementById('color-palette').children
for (let i = 0; i < colorsFromPalette.length; i++) {
    colorsFromPalette[i].addEventListener('click', selectColor)
}


function selectColor(e) {
    const color = e.target.style.backgroundColor;
    let colorSelected = document.getElementById(e.target.id)
    let colorsList = document.getElementsByClassName("color");
    for (let i = 0; i < colorsList.length; i++) {
        colorsList[i].className = "color";
    }
    colorSelected.className = "color selected"

}

const pixelToPaint = document.querySelector('#pixel-board')
pixelToPaint.addEventListener('click', colorPixel)

function colorPixel(e) {
    let colorSelected = document.querySelector('#color-palette .selected')
    let colorToSet = colorSelected.style.backgroundColor

    let row = e.target.id.split(":")[0];
    let col = e.target.id.split(":")[1];
    if (row === undefined || col === undefined || isNaN(row) || isNaN(col)) {
        return;
    }
    console.log(row, col, colorToSet)
    if (sendPixel(row, col, colorToSet, accessToken)) {
        let pixel = e.target
        pixel.style.backgroundColor = colorToSet
        stompClient.send('/pixel/editor', {}, row, col, colorToSet)
        displayPixelsAvailable()
        fetchMatrix()
    }
}

const buttonToClear = document.getElementById('clear-board')
buttonToClear.addEventListener('click', clearPixels)

function clearPixels() {
    let lengthOFTable = document.getElementsByClassName('pixel').length

    for (let index = 0; index < lengthOFTable; index += 1) {
        document.getElementsByClassName('pixel')[index].style.background = "white"
    }
}

let newRow = document.createElement('TR')

let newColumn = document.createElement('TD')
for (let index = 0; index < HEIGHT; index += 1) {
    newRow = document.createElement('TR')
    document.querySelector('#table-of-pixel').appendChild(newRow)
    createLineOfTable(WIDTH, index)
}

function createLineOfTable(size, rowIndex) {
    let ops = document.querySelector('#table-of-pixel').lastChild
    for (let index = 0; index < size; index += 1) {
        newColumn = document.createElement('TD')
        newColumn.id = rowIndex + ":" + index;
        ops.appendChild(newColumn).className = 'pixel'
    }
}

function displayPixelsAvailable() {
    countPixels = fetchUserPixelAvailable(accessToken);
    document.getElementById('title').innerText = "Доступно пикселей : " + countPixels;
}

function setTableSize() {
    $.ajax({
        method: "GET",
        url: "/twitch-bot/pixel/size",
        async: false,
        success: function (data) {
            WIDTH = data.width
            HEIGHT = data.height
        }
    })
}

function sendPixel(row, col, color, token) {
    if (countPixels > 0) {
        let urlForSendPixel = "./editor?token=" + token;
        let data = {
            "col": col,
            "row": row,
            "color": color
        }
        $.ajax({
            method: "POST",
            url: urlForSendPixel,
            data: JSON.stringify(data),
            contentType: 'application/json',
            async: false
        })
        return true;
    }
    return false;
}

function fetchUserPixelAvailable(token) {
    let count = 0
    $.ajax({
        method: "GET",
        url: "/twitch-bot/pixel/redeemed-pixels",
        data: {"token": token},
        contentType: 'application/json',
        async: false,
        success: function (data) {
            count = data
        }
    })
    return count;
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
    for (let pixel in data) {
        let row = pixel.split(":")[0]
        let col = pixel.split(":")[1]
        if (row < HEIGHT && col < WIDTH) {
            document.getElementById(pixel).style.background = data[pixel]
        }
    }
}