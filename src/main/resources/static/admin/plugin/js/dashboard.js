function showData() {
    // Hiển thị table chứa dữ liệu
    $("#myDiv table").hide()
    $("#myDiv4 table").hide();
    $("#myDiv3 table").hide();
    $("#myDiv2 table").show();

}
function showData2() {
    // Hiển thị table chứa dữ liệu
    $("#myDiv table").hide()
    $("#myDiv4 table").hide();
    $("#myDiv2 table").hide();
    $("#myDiv3 table").show();

}
function showData3() {
    // Hiển thị table chứa dữ liệu
    $("#myDiv table").hide()
    $("#myDiv3 table").hide();
    $("#myDiv2 table").hide();
    $("#myDiv4 table").show();

}
function showData4() {
    // Hiển thị table chứa dữ liệu
    $("#myDiv3 table").hide();
    $("#myDiv2 table").hide();
    $("#myDiv4 table").hide();
    $("#myDiv table").show();

}
function oncl(id) {
    window.location.href = "http://localhost:8080/admin/bill/bill_detail/" + id
}
$(document).ready(function () {
    var defaultValue = $("#completed").attr("defaultValue");
    $("#date").change(function () {
        var date = $(this).val();
        $.ajax({
            url: "/admin/dashboard/list",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (myData) {
                if(myData==null){
                    $("#completed").val(defaultValue);
                }else {
                    $("#completed").text(myData.length);
                }
            }
        })
        $.ajax({
            url: "/admin/dashboard/listReturn",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (DataReturn) {
                $("#return").text(DataReturn.length);
            }
        });
        $.ajax({
            url: "/admin/dashboard/listProcessing",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (DataProcessing) {
                $("#processing").text(DataProcessing.length);
            }
        });
    });
});