$(".point").on("click", function () {
    if (this.classList.contains("star1")) {
        // If the class is "original", change it to "highlight"
        this.classList.remove("bx-star");
        this.classList.add("bxs-star");
        if ($(".star2.bxs-star")) {
            $(".star2.bxs-star").toggleClass("bxs-star bx-star");
        }
        if ($(".star3.bxs-star")) {
            $(".star3.bxs-star").toggleClass("bxs-star bx-star");
        }
        if ($(".star4.bxs-star")) {
            $(".star4.bxs-star").toggleClass("bxs-star bx-star");
        }
        if ($(".star5.bxs-star")) {
            $(".star5.bxs-star").toggleClass("bxs-star bx-star");
        }
        point = 1
    }
    if (this.classList.contains("star2")) {
        // If the class is "original", change it to "highlight"
        this.classList.remove("bx-star");
        this.classList.add("bxs-star");
        if ($(".star1.bx-star")) {
            $(".star1.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star3.bxs-star")) {
            $(".star3.bxs-star").toggleClass("bxs-star bx-star");
        }
        if ($(".star4.bxs-star")) {
            $(".star4.bxs-star").toggleClass("bxs-star bx-star");
        }
        if ($(".star5.bxs-star")) {
            $(".star5.bxs-star").toggleClass("bxs-star bx-star");
        }
        point = 2
    }
    if (this.classList.contains("star3")) {
        // If the class is "original", change it to "highlight"
        this.classList.remove("bx-star");
        this.classList.add("bxs-star");
        if ($(".star1.bx-star")) {
            $(".star1.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star2.bx-star")) {
            $(".star2.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star4.bxs-star")) {
            $(".star4.bxs-star").toggleClass("bxs-star bx-star");
        }
        if ($(".star5.bxs-star")) {
            $(".star5.bxs-star").toggleClass("bxs-star bx-star");
        }
        point = 3
    }
    if (this.classList.contains("star4")) {
        // If the class is "original", change it to "highlight"
        this.classList.remove("bx-star");
        this.classList.add("bxs-star");
        if ($(".star1.bx-star")) {
            $(".star1.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star2.bx-star")) {
            $(".star2.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star3.bx-star")) {
            $(".star3.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star5.bxs-star")) {
            $(".star5.bxs-star").toggleClass("bxs-star bx-star");
        }
        point = 4
    }
    if (this.classList.contains("star5")) {
        // If the class is "original", change it to "highlight"
        this.classList.remove("bx-star");
        this.classList.add("bxs-star");
        if ($(".star1.bx-star")) {
            $(".star1.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star2.bx-star")) {
            $(".star2.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star3.bx-star")) {
            $(".star3.bx-star").toggleClass("bx-star bxs-star");
        }
        if ($(".star4.bx-star")) {
            $(".star4.bx-star").toggleClass("bx-star bxs-star");
        }
        point = 5
    }
    // else {
    //     // If the class is not "original", change it back to "original"
    //     myParagraph.classList.remove("highlight");
    //     myParagraph.classList.add("original");
    // }
    // this.className = 'bx bxs-star bx-lg star1 point '

})
$("#sendEvaluate").on("click", function () {
    uploadImage()

    var image = []
    $.each($(".file-input"), function (index, item) {

        if (item.value != '') {
            var fileName = item.value; // Lấy đường dẫn đầy đủ của tệp
// Trích xuất tên tệp từ đường dẫn
            var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
            if (lastIndex >= 0) {
                fileName = fileName.substr(lastIndex + 1);
            }
            image.push(fileName)
        }
    })
    var data = {
        point: point,
        comment: $("#comment").val(),
        product: $("#sendEvaluate").val(),
        image: image
    }
    $.ajax({
        url: "/product/evaluate/add",
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (data) {
            const Toast = Swal.mixin({
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.onmouseenter = Swal.stopTimer;
                    toast.onmouseleave = Swal.resumeTimer;
                }
            });
            Toast.fire({
                icon: "success",
                title: "Thêm thành công"
            });
           window.location.reload()
        }
    })
})

function uploadImage() {
    var fileInput = document.getElementsByClassName('file-input');
    var formData = new FormData();
    $.each(fileInput, function (index, item) {
        if (item.files[0]) {
            formData.append('images', item.files[0], item.files[0].name);
        }
    })

    $.ajax({
        url: '/product/upload',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            // Xử lý kết quả từ máy chủ (nếu cần)
            console.log("thành công")
        },
        error: function (error) {
            console.error('Lỗi:', error);
        }
    });
}