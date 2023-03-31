let color = "red";

$(document).ready(function () {
    let colorFromParameter = (new URL(document.location)).searchParams.get("color");
    if (colorFromParameter !== null) {
        color = colorFromParameter;
    }

    updateTable();
    setInterval(function () {
        updateTable();
    }, 60 * 1000);

});

function updateTable() {
    let reviewList = getReview();
    $("#tableBody").empty();

    let now = new Date();

    $("#tableBody").attr("style", "color: " + color);

    for (let review of reviewList) {
        let dateTime = Date.parse(review.bookedDateTime);
        let oneHourInMilliseconds = 1000 * 60 * 60;
        let isCurrentReview = ((now - dateTime) > 0) & ((now - dateTime) < oneHourInMilliseconds);
        let isPreviousReview = (now - dateTime) > oneHourInMilliseconds;
        $("#tableBody").append("" +
            "<tr" + (isCurrentReview ? " class=\"bg-success p-2 bg-opacity-50\"" : isPreviousReview ? " style=\"opacity: 0.6\"" : "") + ">\n" +
            "      <td>" + review.title + "</td>\n" +
            "      <td>" + review.bookedDateTime.substring(11) + "</td>\n" +
            "    </tr>")
    }

}

function getReview() {
    let reviewList;
    $.ajax({
        url: "/incoming-reviews",
        dataType: 'json',
        type: 'GET',
        contentType: "application/json",
        async: false,
        success: function (response) {
            reviewList = response;
        }
        // console.log(response)
    })
    return reviewList;
}