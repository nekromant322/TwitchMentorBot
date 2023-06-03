window.onload = function () {
    refreshTable();
}

function addCommand() {
    let command = {
        name: $("#adding-command-name").val(),
        response: $("#adding-response").val(),
        period: $("#adding-period").val(),
    }
    let commandjson = JSON.stringify(command);
    $.ajax({
        method: "POST",
        url: "./commands/add",
        data: commandjson,
        contentType: "application/json; charset=utf8",
        success: function () {
            refreshAddCommand();
            document.getElementById("commands-table-shower").click();
        },
        error: function () {
            alert('Bad credentials');
        }
    })
}

function refreshAddCommand() {
    $("#adding-command-name").val('');
    $("#adding-response").val('');
    $("#adding-period").val('');
}

function refreshTable() {
    $("#commands-table td").remove();
    $.ajax({
        method: 'GET',
        url: './commands/list',
        contentType: 'application/json',
        success: function (response) {
            drawTable(response);
            refreshAddCommand();
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function drawTable(data) {
    for (let i = 0; i < data.length; i++) {
        addRow(data[i]);
    }
}

function addRow(data) {
    let table = document.getElementById("commands-table").getElementsByTagName("tbody")[0];
    let tr = table.insertRow(table.rows.length);

    insertTd("!" + data.name, tr);
    insertTd(data.response, tr);
    insertTd(data.period, tr);
    insertEnableFlag(data, tr);
    insertEditBtn(data, tr);
    insertDelBtn(data, tr);
}

function insertTd(value, parent) {
    let element = document.createElement("td");
    element.scope = "row";
    element.innerText = value;
    parent.insertAdjacentElement("beforeend", element);
}

function insertEnableFlag(data, parent) {
    let td = document.createElement("td");
    td.scope = "row";
    let element = document.createElement("input");
    element.type = "checkbox";
    element.checked = data.enabled;

    element.addEventListener('change', () => {
        switchToggle(data);
    });

    td.appendChild(element);
    parent.insertAdjacentElement("beforeend", td);
}

function switchToggle(data) {
    let command = {
        id: data.id,
        name: data.name,
        response: data.response,
        period: data.period,
        enabled: data.enabled = data.enabled !== true
    }
    let commandjson = JSON.stringify(command)
    $.ajax({
        method: 'POST',
        url: "./commands/edit/" + data.id.toString(),
        data: commandjson,
        contentType: "application/json; charset=utf8",
        success: function () {
            refreshTable();
        },
        error: function () {
            alert('sth went wrong');
        }
    });
}

function insertDelBtn(data, parent) {
    let td = document.createElement("td");
    td.scope = "row";
    let element = document.createElement("button");
    element.innerText = "Delete";
    element.type = "submit";
    element.className = "btn btn-danger";
    element.addEventListener('click', () => {
        drawDeleteModal(data);
        $('#delete-command-modal').modal('show');
    })
    td.appendChild(element);
    parent.insertAdjacentElement("beforeend", td);
}

function insertEditBtn(data, parent) {
    let td = document.createElement("td");
    td.scope = "row";
    let element = document.createElement("button");
    element.innerHTML = "<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#edit-user-modal\">"
    element.innerText = "Edit";
    element.className = "btn btn-info";
    element.addEventListener('click', () => {
        drawEditModal(data);
        $('#edit-command-modal').modal('show');
    })
    td.appendChild(element);
    parent.insertAdjacentElement("beforeend", td);
}

function drawEditModal(data) {
    $.ajax({
        method: 'GET',
        url: './commands/edit/' + data.id.toString(),
        contentType: 'application/json',
        success: function () {
            $("#edit-command").val(data.name);
            $("#edit-response").val(data.response);
            $("#edit-period").val(data.period);

            document.getElementById('edit-button').onclick = (function () {
                let command = {
                    id: data.id,
                    name: $("#edit-command").val(),
                    response: $("#edit-response").val(),
                    period: $("#edit-period").val(),
                    enabled: data.enabled
                }
                let commandjson = JSON.stringify(command)
                $.ajax({
                    method: 'POST',
                    url: "./commands/edit/" + data.id.toString(),
                    data: commandjson,
                    contentType: "application/json; charset=utf8",
                    success: function () {
                        document.getElementById("close-edit").click();
                        refreshTable();
                    },
                    error: function () {
                        alert('Bad Credentials. Please try again');
                    }
                });
            });
        }
    });
}

function drawDeleteModal(data) {
    document.getElementById('delete-button').onclick = (function () {
        $.ajax({
            method: 'GET',
            url: "./commands/delete/" + data.id.toString(),
            success: function () {
                $('#close-delete').trigger('click');
                refreshTable();
            }
        })
    })
}
