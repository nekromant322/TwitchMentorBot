window.onload = function () {
    getAllSongs();
}

function addSong() {
    let artist = $('#adding-artist').val();
    let name = $('#adding-name').val();
    let chordsLink = $('#adding-chords-link').val();
    let comment = $('#adding-comment').val();

    $.ajax({
        type: 'POST',
        url: '/songs',
        async: false,
        data: JSON.stringify({
            'artist': artist,
            'name': name,
            'chordsLink': chordsLink,
            'comment': comment
        }),
        contentType: 'application/json; charset=utf8',
        success: [function () {
            clearAddSong();
            document.getElementById("song-list-table-shower").click();
        }],
        error: ['Ошибка: проверьте введенные данные']
    })
}

function clearAddSong() {
    $('#adding-artist').val('');
    $('#adding-name').val('');
    $('#adding-chords-link').val('');
    $('#adding-comment').val('');
}

function getAllSongs() {
    $.get('/songs/list', function (data) {

        let table = "";

        for (i = 0; i < data.length; i++) {
            table = table + "<tr>" +
                "<td>" + data[i].artist + "</td>" +
                "<td>" + data[i].name + "</td>" +
                "<td>" + data[i].chordsLink + "</td>" +
                "<td>" + data[i].comment + "</td>" +
                "<td><button onclick='editSong(" + data[i].id + ")' " +
                "type='button' " +
                "class='btn btn-success' " +
                "data-bs-toggle='modal' " +
                "data-bs-target='#modalEditSong'>Edit</button></td>" +
                "<td><button onclick='deleteSong(" + data[i].id + ")' " +
                "type='button' " +
                "class='btn btn-danger' " +
                "data-bs-toggle='modal' " +
                "data-bs-target='#modalDeleteSong'>Delete</button></td>" +
                "</tr>";
        }

        $('#song-list-table-body').html(table);
    });
}

//Отображение модалки Edit
function editSong(id) {
    $('#modal-edit').modal({
        show: true
    })

    $.ajax({
        url: '/songs/' + id,
        type: 'GET',
        success: function (data) {
            $('#edit-id').val(data.id).attr('disabled', 'disabled');
            $('#edit-name').val(data.name);
            $('#edit-artist').val(data.artist);
            $('#edit-chords-link').val(data.chordsLink);
            $('#edit-comment').val(data.comment);
        }
    })
}

//Действие внутри модалки Edit
$('#edit-button').click(function () {
    let id = $('#edit-id').val();
    let artist = $('#edit-artist').val();
    let name = $('#edit-name').val();
    let chordsLink = $('#edit-chords-link').val();
    let comment = $('#edit-comment').val();

    $.ajax({
        url: '/songs/' + id,
        type: 'POST',
        cache: false,
        async: false,
        data: JSON.stringify({
            'artist': artist,
            'name': name,
            'chordsLink': chordsLink,
            'comment': comment
        }),
        contentType: 'application/json',
        error : [function(jqXHR, textStatus, errorThrown){
            alert(textStatus)
        }],
        success: [function () {
            $('#modal-edit').modal({
                show: false
            })
            getAllSongs();
        }]
    })
});

//Отображение модалки Delete
function deleteSong(id) {
    $('#modal-delete').modal({
        show: true
    })

    $.ajax({
        url: '/songs/' + id,
        type: 'GET',
        success: function (data) {
            $('#delete-id').val(data.id).attr('disabled', 'disabled');
            $('#delete-name').val(data.name).attr('disabled', 'disabled');
            $('#delete-artist').val(data.artist).attr('disabled', 'disabled');
            $('#delete-chords-link').val(data.chordsLink).attr('disabled', 'disabled');
            $('#delete-comment').val(data.comment).attr('disabled', 'disabled');
        }
    })
}

//Действие внутри модалки Delete
$('#delete-button').click(function () {
    let id = $('#delete-id').val();

    $.ajax({
        url:'/songs/' + id,
        type: 'DELETE',
        cache: false,
        async: false,
        contentType: 'application/json',
        error : [function(jqXHR, textStatus, errorThrown){
            alert(textStatus)
        }],
        success: [function () {
            getAllSongs();
        }]
    })
})