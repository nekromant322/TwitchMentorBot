window.onload = function () {
    getDailyBonusList();
}

function getDailyBonusList() {
    $.get('./bonuses', function (data) {

        let table = "";

        for (i = 0; i < data.length; i++) {
            table = table + "<tr>" +
                "<td>" + data[i].twitchUserName + "</td>" +
                "<td>" + data[i].points + "</td>" +
                "<td>" + data[i].lastTimeUsed + "</td>" +
                "</tr>";
        }

        $('#daily-bonus-table-body').html(table);
    });
}

function getTxtDailyBonusListForPointAucImport() {
    $.ajax({
        url: './bonuses/file',
        type: 'GET',
        async: false,
        success: function (data) {
            let json = JSON.stringify(data);
            let blob = new Blob([json], {type: "text"});
            let url = URL.createObjectURL(blob);
            let a = document.createElement('a');
            let date = new Date();

            a.download = date.toLocaleDateString('pt-PT') + '_розыгрыш';
            a.href = url;
            a.click();
        }
    })
}
