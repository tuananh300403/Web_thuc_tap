var idBill;
$.each(document.getElementsByClassName("return"), function (index, item) {
    item.addEventListener('click', function () {
        idBill = item.value;
        $.each(document.getElementsByClassName("checkbox" + idBill), function (index, item) {
            item.addEventListener('change', function () {
                var nameProduct = document.getElementsByClassName("nameProduct" + idBill)
                var quantityReturn = document.getElementsByClassName("quantityReturn" + idBill)
                var image = document.getElementsByClassName("returnImg" + item.value)
                var reason = document.getElementsByClassName("reason" + idBill)
                var completed=document.getElementsByClassName("completed"+idBill)
                if (item.checked == false) {
                    nameProduct[index].disabled = true;
                    quantityReturn[index].disabled = true;
                    reason[index].disabled = true;
                    $.each(image, function (index, item) {
                        item.disabled = true;
                    })
                    completed[index].hidden=true;
                } else {
                    nameProduct[index].disabled = false;
                    quantityReturn[index].disabled = false;
                    reason[index].disabled = false;
                    $.each(image, function (index, item) {
                        item.disabled = false;
                    })
                    completed[index].hidden=false;
                }
            })
            document.getElementById("checkboxAll" + idBill).addEventListener("click", function () {
                var nameProduct = document.getElementsByClassName("nameProduct" + idBill)
                var quantityReturn = document.getElementsByClassName("quantityReturn" + idBill)
                var image = document.getElementsByClassName("returnImg" + item.value)
                var reason = document.getElementsByClassName("reason" + idBill)
                var completed=document.getElementsByClassName("completed"+idBill)
                if (this.checked) {
                    $.each($(".checkbox" + idBill), function (index, item) {
                        item.checked = true;
                        nameProduct[index].disabled = false;
                        quantityReturn[index].disabled = false;
                        reason[index].disabled = false;
                        $.each(image, function (index, item) {
                            item.disabled = false;
                        })
                        completed[index].hidden=false;
                    })
                } else {
                    $.each($(".checkbox" + idBill), function (index, item) {
                        item.checked = false;
                        nameProduct[index].disabled = true;
                        quantityReturn[index].disabled = true;
                        reason[index].disabled = true;
                        $.each(image, function (index, item) {
                            item.disabled = true;
                        })
                        completed[index].hidden=true;
                    })
                }
            })
        })
    })
})


function clickSave() {

    var idBillProduct = document.getElementsByClassName("checkbox" + idBill)
    var nameProduct = document.getElementsByClassName("nameProduct" + idBill)
    var quantityReturn = document.getElementsByClassName("quantityReturn" + idBill)
    var reason = document.getElementsByClassName("reason" + idBill)
    var data = []
    var check = true;
    $.each(idBillProduct, function (index, item) {
        var errorQuantity = document.querySelector(".errorQuantity" + item.value);
        var errorReason = document.querySelector(".errorReason" + item.value);
        if(item.checked) {
            if (quantityReturn[index].value > Number(quantityReturn[index].getAttribute("max"))) {
                errorQuantity.innerHTML = "Số lượng trả không lớn hơn số lượng mua!"
                check = false;
                console.log(errorQuantity);
            } else if (quantityReturn[index].value.length == 0) {
                errorQuantity.innerHTML = "Vui lòng điền số lượng!"
                check = false;
            } else {
                errorQuantity.innerHTML = ""
            }
            if (reason[index].value.length == 0) {
                errorReason.innerHTML = "Vui lòng nhập lí do!"
                check = false;
            } else {
                errorReason.innerHTML = ""
            }
        }
    })
    $.each(idBillProduct, function (index, item) {
        if (item.disabled == false) {
            var image = "returnImg" + item.value
            var arrImage = []
            var listImage = document.getElementsByClassName(image)
            var errorImage = document.querySelector(".errorImage" + item.value)
            $.each(listImage, function (index, itm) {
                var imageItem;
                var fileName = itm.value; // Lấy đường dẫn đầy đủ của tệp
                var imageName;
                // Trích xuất tên tệp từ đường dẫn
                var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
                if (lastIndex >= 0) {
                    imageName = fileName.substr(lastIndex + 1);
                }

                if (fileName.length != 0) {
                    imageItem = {
                        nameImage: imageName,
                        idBillProduct: item.value
                    }
                    arrImage.push(imageItem)
                }


            })
            if(item.checked) {
                if (arrImage.length == 0) {
                    errorImage.innerHTML = "Vui lòng tải lên ít nhất 1 ảnh!"
                    check = false;
                } else {
                    errorImage.innerHTML = ""
                }
            }
            var temp = {
                idBillProduct: item.value,
                nameProduct: nameProduct[index].value,
                quantityReturn: quantityReturn[index].value,
                reason: reason[index].value,
                image: arrImage,

            }
            data.push(temp)
        }
    })
    if (check == false) {
        return
    }
    uploadImage();
    $.ajax({
        url: "/return/" + idBill,
        method: "post",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function () {
            var close = document.getElementsByClassName("close" + idBill)
            $.each(close, function (index, item) {
                item.click();
            })
            console.log("Thành công!")
            window.location.reload();
        }
    })
}

function uploadImage() {
    var fileInput = document.getElementsByClassName('file-input');
    var formData = new FormData();
    $.each(fileInput, function (index, item) {
        console.log(item.value)
        if (item.value.trim().length != 0) {
            formData.append('images', item.files[0], item.files[0].name);
        }
    })
    console.log(formData)
    $.ajax({
        url: '/product/upload',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            console.log("thành công")
        },
        error: function (error) {
            console.error('Lỗi:', error);
        }
    });
}
