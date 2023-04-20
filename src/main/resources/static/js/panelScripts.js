window.onload = function () {
    refreshTable();
}

function addCommand() {
    let command = {
        command_name: $("#adding-command-name").val(),
        command_response: $("#adding-response").val(),
    }
    let commandjson = JSON.stringify(command);
    $.ajax({
        method: "POST",
        url: "/twitch-bot/commands/new",
        data: commandjson,
        contentType: "application/json; charset=utf8",
        success: function () {
            $("#commands-table-show").click();
        },
        error: function () {

            alert('Bad credentials');
        }
    })
}

function refreshAddCommand() {
    $("adding-command-name").value("");
    $("adding-response").value("");
}

function refreshTable() {
    $("#commands-table td").remove();
    $.ajax({
        method: 'GET',
        url: '/twitch-bot/commands',
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
    let roles = "";
    data.roles.forEach(function (item) {
        roles += item.role + " ";
    });

    insertTd(data.command_name, tr);
    insertTd(data.command_response, tr);
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
    element.className = "checkbox";
    element.innerHTML = "<span class=\"slider round\"></span>";
    element.addEventListener('change', () => {
        alert('changed');
    })
    td.appendChild(element);
    parent.insertAdjacentElement("beforeend", element);
}

function insertDelBtn(data, parent) {
    let td = document.createElement("td");
    td.scope = "row";
    let element = document.createElement("button");
    element.innerText = "Delete";
    element.type = "submit";
    element.className = "btn btn-danger"
    element.addEventListener('click', () => {
        drawDeleteModal(data);
        $('#delete-user-modal').modal('show');
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
        drawEditModal(data)
        $('#edit-user-modal').modal('show');
    })
    td.appendChild(element);
    parent.insertAdjacentElement("beforeend", td);
}

function drawEditModal(data) {
    $.ajax({
        method: 'GET',
        url: '/twitch-bot/commands/edit' + data.id.toString(),
        contentType: 'application/json',
        success: function (data) {
            document.getElementById('edit-role-user').checked = false;
            document.getElementById('edit-role-admin').checked = false;
            data.roles.forEach(function (item) {
                if ((item.role) === 'USER') {
                    document.getElementById('edit-role-user').checked = true;
                }
                if ((item.role) === 'ADMIN') {
                    document.getElementById('edit-role-admin').checked = true;
                }
            })
            document.getElementById('edit-name').value = data.name;
            document.getElementById('edit-age').value = data.age;
            document.getElementById('edit-email').value = data.email;
            document.getElementById('edit-password').value = data.password;
            document.getElementById('edit-button').onclick = (function () {
                let roles = [];

                if (document.getElementById('edit-role-user').checked === true) {
                    roles.push({id: 1, name: "ROLE_USER", authority: "ROLE_USER"})
                }

                if (document.getElementById('edit-role-admin').checked === true) {
                    roles.push({id: 2, name: "ROLE_ADMIN", authority: "ROLE_ADMIN"})
                }

                let user = {
                    id: data.id,
                    name: document.getElementById('edit-name').value,
                    age: document.getElementById('edit-age').value,
                    email: document.getElementById('edit-email').value,
                    password: document.getElementById('edit-password').value,
                    roles: roles
                }
                let userjson = JSON.stringify(user)
                $.ajax({
                    method: 'POST',
                    url: "/twitch-bot/commands/edit" + data.id.toString(),
                    data: userjson,
                    contentType: "application/json; charset=utf8",
                    success: function () {
                        document.getElementById('close-edit').click();
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
    document.getElementById('delete-name').value = data.name;
    document.getElementById('delete-age').value = data.age;
    document.getElementById('delete-email').value = data.email;
    document.getElementById('delete-password').value = data.password;
    document.getElementById('delete-button').onclick = (function () {
        $.ajax({
            method: 'GET',
            url: "/twitch-bot/commands/delete/" + data.id.toString(),
            success: function () {
                document.getElementById('close-delete').click()
                refreshTable()
            }
        })
    })
}
