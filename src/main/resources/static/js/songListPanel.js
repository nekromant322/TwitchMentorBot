window.onload = function () {
    getAllSongs();
}

function getAllSongs() {
    $.get('/songList/panel/list', function (data) {

        let table = "";

        for (i = 0; i < data.length; i++) {
            table = table + "<tr>" +
                "<td>" + data[i].artist + "</td>" +
                "<td>" + data[i].name + "</td>" +
                "<td><button onclick='copySong(" + data[i].id + ")' " +
                "type='button' " +
                "class='btn btn-info'>Copy</button></td>" +
                "</tr>";
        }

        $('#song-list-table-body').html(table);
    });
}

function copySong(id) {
   $.ajax({
       url: '/songList/' + id,
       type: 'GET',
       success: function (data) {
           navigator.clipboard.writeText(data.artist + ' - ' + data.name);
       }
   })
}