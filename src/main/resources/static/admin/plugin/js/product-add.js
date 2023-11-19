var dataProductDetail = {}
$(document).ready(function () {
    loadDataField()
    loadDataGroup()
    loadVariant()
    // getProduct()
})
// $("#select-group").change(function () {
//     loadDataType()
// })
$("#select-type").change(function () {
    changeTableProductDetail()
})

// document.getElementById("sameProduct").addEventListener("keyup", function (e) {
//     var sameCode = this.value
//     $.ajax({
//         url: "/product/same-product?same-code=" + sameCode,
//         method: "get",
//         success: function (data) {
//             console.log(data)
//         }
//     })
// })
// document.getElementById("sameProduct").addEventListener("paste", function (e) {
//     const clipboardData = e.clipboardData || window.clipboardData;
//     const sameCode = clipboardData.getData('text');
//     $.ajax({
//         url: "/product/same-product?same-code=" + sameCode,
//         method: "get",
//         success: function (data) {
//             console.log(data)
//         }
//     })
// })

function loadDataField() {
    $.ajax({
        url: "/field/all",
        method: "get",
        success: function (data) {
            // var dataTable = $('#tableAttributes tbody');
            $("#tableAttributes tbody").empty()
            $.each(data, function (index, item) {
                if (item.variant == false) {
                    var button = document.createElement('button');
                    button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                    button.className = "btn btn-outline-secondary addAttributes mx-2"
                    button.value = item.name
                    button.id = "_" + item.id

                    var label = document.createElement('label')
                    button.appendChild(label)
                    // Tạo một ô trong bảng
                    var cell1 = document.createElement('td');
                    var cell2 = document.createElement('td');
                    var cell3 = document.createElement('td');

                    var tbody = document.getElementById('tableAttributes').getElementsByTagName('tbody')[0];

                    cell1.innerText = document.getElementById('tableAttributes').rows.length
                    cell2.innerText = item.name
                    cell3.appendChild(button); // Thêm nút vào ô
                    // Lấy bảng theo ID
                    var indexRow = tbody.querySelectorAll("tr")

                    // Thêm ô (cell) vào dòng (row) trong bảng
                    var row = tbody.insertRow($("tableAttributes").rows); // Thay đổi chỉ số hàng theo ý muốn
                    row.appendChild(cell1);
                    row.appendChild(cell2);
                    row.appendChild(cell3);
                }
            });
        },
    })

}

function loadVariant() {
    $.ajax({
        url: "/field/all",
        method: "get",
        success: function (data) {
            // var dataTable = $('#tableAttributes tbody');
            $("#table-variant tbody").empty()
            $.each(data, function (index, item) {
                if (item.variant == true) {
                    var button = document.createElement('button');
                    button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                    button.className = "btn btn-outline-secondary add-variant mx-2"
                    button.value = item.name
                    button.id = "_" + item.id

                    var label = document.createElement('label')
                    button.appendChild(label)
                    // Tạo một ô trong bảng
                    var cell1 = document.createElement('td');
                    var cell2 = document.createElement('td');
                    var cell3 = document.createElement('td');

                    var tbody = document.getElementById('table-variant').getElementsByTagName('tbody')[0];


                    cell1.innerText = document.getElementById('table-variant').rows.length
                    cell2.innerText = item.name
                    cell3.appendChild(button); // Thêm nút vào ô
                    // Lấy bảng theo ID

                    // Thêm ô (cell) vào dòng (row) trong bảng
                    var row = tbody.insertRow($("table-variant").rows); // Thay đổi chỉ số hàng theo ý muốn
                    row.appendChild(cell1);
                    row.appendChild(cell2);
                    row.appendChild(cell3);
                }
            });
        },
    })
}

// function loadDataType() {
//     let value = $("#select-group").val()
//     $.ajax({
//         url: "/type/all?group=" + value,
//         method: "get",
//         success: function (data) {
//             var select = $('#select-type');
//             select.empty();
//             select.append($('<option>', {
//                 disabled: true,
//                 selected: true,
//                 hidden: true,
//                 value: -1,
//                 text: "--Chưa chọn--"
//             }))
//             $.each(data, function (index, item) {
//                 select.append($('<option>', {
//                     value: item.id,
//                     text: item.nameType
//                 }));
//             });
//         },
//     })
//
// }

function loadDataGroup() {
    $.ajax({
        url: "/group/all",
        method: "get",
        success: function (data) {
            var select = $('#select-group');
            select.empty();
            $.each(data, function (index, item) {
                select.append($('<option>', {
                    value: item.id,
                    text: item.nameGroup
                }));
            });
        },
    })
}


function clickImage() {
    if (this) {
        // Lấy tham chiếu đến phần tử cha của input (ở đây, phần tử cha là thẻ "li")
        var parentElement = this.closest('li');
        if (parentElement) {
            // Tìm thẻ img bên trong phần tử cha
            var inputFile = parentElement.querySelector('input[type="file"]');
            if (inputFile) {
                // Bạn đã tìm thấy thẻ img, bạn có thể làm việc với nó ở đây.
                inputFile.click()
            }
        }
    }
}

function onchangeImage() {
    if (this) {
        var parentElement = this.closest('li');
        if (parentElement) {
            var imageElement = parentElement.querySelector("img.image-preview")
            const file = this.files[0]; // Lấy tệp đã chọn
            if (file && imageElement) {
                // Kiểm tra xem tệp đã chọn có phải là hình ảnh
                if (file.type.startsWith('image/')) {
                    // Tạo đường dẫn URL cho hình ảnh và hiển thị nó
                    const imageURL = URL.createObjectURL(file);
                    imageElement.src = imageURL;
                } else {
                    alert('Vui lòng chọn một tệp hình ảnh.');
                    this.value = ''; // Đặt lại trường nhập
                }
            } else {
                imageElement.src = "/oanh"
            }
        }
    }
}

function saveProduct() {
    document.getElementById("save-product").removeEventListener("click", clickSave)
    document.getElementById("save-product").addEventListener("click", clickSave)
}

function clickSave() {
    var valueReturn = validate()
    if (valueReturn === false) {
        return
    }
    var sku = document.getElementsByClassName("sku")
    var priceImport = document.getElementsByClassName("priceImport")
    var priceExport = document.getElementsByClassName("priceExport")
    var quantity = document.getElementsByClassName("quantity")
    var checkActive = document.getElementsByClassName("check-active")

    var data = {}
    data.listProduct = []
    data.product = []
    console.log(document.getElementById("table-product-detail").rows)

    // set ảnh

    $.each(checkActive, function (index, item) {
        var value = item.getAttribute("value")
        var image = "imageUpload" + value
        var arrImage = []
        var listImage = document.getElementsByClassName(image)
        $.each(listImage, function (index, item) {
            // Lấy tệp từ trường chọn tệp
            var imageItem;
            var fileName = item.value; // Lấy đường dẫn đầy đủ của tệp
// Trích xuất tên tệp từ đường dẫn
            var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
            if (lastIndex >= 0) {
                fileName = fileName.substr(lastIndex + 1);
            }

            if (item.classList.contains("true")) {
                imageItem = {
                    location: "true",
                    multipartFile: fileName
                }
            } else {
                imageItem = {
                    location: "false",
                    multipartFile: fileName
                }
            }
            arrImage.push(imageItem)
        })
        console.log(sku[index])
        var temp = {
            sku: sku[index].value,
            priceImport: priceImport[index].value,
            priceExport: priceExport[index].value,
            quantity: quantity[index].value,
            image: arrImage,
            listAttributes: dataProductDetail.listAttributes[index],
            active: checkActive[index].checked
        }
        data.listProduct.push(temp)
        data.nameProduct = document.getElementById("name-display").value
        data.sameProduct = document.getElementById("sameProduct").value
        data.sku = document.getElementById("sku-code").value
        data.group = document.getElementById("select-group").value
    })
    var inputAttributes = document.querySelectorAll(".input-data.data-attributes")
    $.each(inputAttributes, function (index, item) {
        // var arr = []
        var allTag = document.querySelector("." + item.id)
        data.product.push({
            id: item.id.substring(2),
            value: allTag.textContent,
        })
    })
    console.log(data)
    uploadImage()
    $.ajax({
        url: "/product/save-product",
        method: "post",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function () {
            $("#table-product-detail tbody").empty()
            clear()
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
                title: "Thêm sản phẩm thành công"
            });
        }
    })
}

function uploadImage() {
    var fileInput = document.getElementsByClassName('file-input');

    var formData = new FormData();
    $.each(fileInput, function (index, item) {
        formData.append('images', item.files[0], item.files[0].name);
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

// Thêm thuộc tính
$("#submit-add-attribute").click(function () {
    var value = $("#name-attribute").val()
    var dataPost = {name: value}
    $.ajax(
        {
            url: "/field/add",
            method: "post",
            data: JSON.stringify(dataPost), // Chuyển đổi dữ liệu thành chuỗi JSON
            contentType: 'application/json',
            success: function (data) {
                // $("#close-add-attribute").click()
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                })
                Toast.fire({
                    icon: 'success',
                    title: 'Thêm thành công thuộc tính ' + data.name,
                    customClass: {
                        container: 'alert' // Tên lớp tùy chỉnh
                    }
                })
                $("#name-attribute").val("")

                var button = document.createElement('button');
                button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                button.className = "btn btn-outline-secondary addAttributes mx-2"
                button.value = data.name
                button.id = "_" + data.id

                var label = document.createElement('label')
                button.appendChild(label)
                // Tạo một ô trong bảng
                var cell1 = document.createElement('td');
                var cell2 = document.createElement('td');
                var cell3 = document.createElement('td');

                var tbody = document.getElementById('tableAttributes').getElementsByTagName('tbody')[0];


                cell1.innerText = document.getElementById('table-variant').rows.length
                cell2.innerText = data.name
                cell3.appendChild(button); // Thêm nút vào ô
                // Lấy bảng theo ID

                // Thêm ô (cell) vào dòng (row) trong bảng
                var row = tbody.insertRow($("tableAttributes").rows); // Thay đổi chỉ số hàng theo ý muốn
                row.appendChild(cell1);
                row.appendChild(cell2);
                row.appendChild(cell3);

            },
            error: function (error) {
                console.log('Lỗi trong quá trình POST yêu cầu:', error);
            }
        }
    )
})
$("#submit-add-variant").click(function () {
    var value = $("#name-variant").val()
    var dataPost = {
        name: value,
        variant: 1
    }
    $.ajax(
        {
            url: "/field/add",
            method: "post",
            data: JSON.stringify(dataPost), // Chuyển đổi dữ liệu thành chuỗi JSON
            contentType: 'application/json',
            success: function (data) {
                // $("#close-add-attribute").click()

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                })

                Toast.fire({
                    icon: 'success',
                    title: 'Thêm thành công biến thể ' + data.name,
                    customClass: {
                        container: 'alert' // Tên lớp tùy chỉnh
                    }
                })
                $("#name-variant").val("")

                var button = document.createElement('button');
                button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                button.className = "btn btn-outline-secondary add-variant mx-2"
                button.value = data.name
                button.id = "_" + data.id

                var label = document.createElement('label')
                button.appendChild(label)
                // Tạo một ô trong bảng
                var cell1 = document.createElement('td');
                var cell2 = document.createElement('td');
                var cell3 = document.createElement('td');

                var tbody = document.getElementById('table-variant').getElementsByTagName('tbody')[0];


                cell1.innerText = document.getElementById('table-variant').rows.length
                cell2.innerText = data.name
                cell3.appendChild(button); // Thêm nút vào ô
                // Lấy bảng theo ID

                // Thêm ô (cell) vào dòng (row) trong bảng
                var row = tbody.insertRow($("table-variant").rows); // Thay đổi chỉ số hàng theo ý muốn
                row.appendChild(cell1);
                row.appendChild(cell2);
                row.appendChild(cell3);
            },
            error: function (error) {
                console.log('Lỗi trong quá trình POST yêu cầu:', error);
            }
        }
    )
})

function myFunction() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("search-input");
    filter = input.value.toUpperCase();
    table = document.getElementById("tableAttributes");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function searchVariant() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("search-variant");
    filter = input.value.toUpperCase();
    table = document.getElementById("table-variant");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

document.addEventListener('click', function (event) {
    if (event.target.classList.contains('addAttributes')) {
        var itemContentDiv = document.querySelector('.item-content');
// Tạo phần tử <div class="attributes">
        var attributesGroupDiv = document.createElement('div');
        attributesGroupDiv.className = 'attributes mb-3';

// Tạo label
        var label = document.createElement("label")
        label.className = "col-form-label"
        label.innerText = event.target.value

        attributesGroupDiv.appendChild(label)
        // div input group
        var inputGroupDiv = document.createElement("div")
        inputGroupDiv.className = "input-group shadow-none"

        var inputDiv = document.createElement("div")
        inputDiv.className = "input-div col-sm-10"
        // tạo textarea
        var input = document.createElement("input")
        input.className = "form-control input-data data-attributes w-100"
        input.rows = 1
        input.id = "_" + event.target.id
        inputDiv.appendChild(input)
        inputGroupDiv.appendChild(inputDiv)

        var deleteDiv = document.createElement('div');
        deleteDiv.className = "col-sm-2 text-center"
        //tạo button delete
        var buttonDel = document.createElement('button')
        buttonDel.type = 'button';
        buttonDel.className = "btn btn-danger delete-button";
        buttonDel.value = event.target.id
        var spanInBtn = document.createElement('span');
        spanInBtn.className = "tf-icons bx bxs-trash delete-button";
        spanInBtn.value = event.target.id

        buttonDel.appendChild(spanInBtn)
        deleteDiv.appendChild(buttonDel)
        inputGroupDiv.appendChild(deleteDiv)
        attributesGroupDiv.appendChild(inputGroupDiv)
        itemContentDiv.appendChild(attributesGroupDiv);

        $.each($(".input-data"), function (index, item) {
            item.addEventListener("keydown", function (e) {
                if (e.key === "Enter" && e.target.value.trim() !== "") {
                    // Tạo một thẻ span cho tag mới
                    const inputValue = e.target.value.trim();
                    const tagValue = inputValue.slice(0);
                    var listValue = []
                    var allTag = document.querySelectorAll("." + e.target.id)

                    $.each(allTag, function (index, item) {
                        listValue.push(item.textContent)
                    })
                    // thêm tag
                    if (allTag.length > 0) {
                        setTimeout(function () {
                            Swal.fire({
                                title: "Thuộc tính chỉ được thêm 1 giá trị",
                                text: "Hãy xóa giá trị cũ nếu muốn thêm giá trị mới",
                                icon: "warning",
                                confirmButtonColor: "#3085d6",
                                confirmButtonText: "Đã hiểu",
                                customClass: {
                                    container: "alert"
                                }
                            })
                        }, 100);
                        return
                    }
                    if (listValue.includes(tagValue)) {
                        // Kiểm tra xem tag đã tồn tại chưa
                        return;
                    }
                    e.target.value = "";

                    const tag = document.createElement("div");
                    tag.textContent = tagValue;
                    tag.classList.add("tag", e.target.id);
                    e.target.closest(".input-div").appendChild(tag);
                    // Xử lý sự kiện khi người dùng nhấp vào tag để xóa

                    tag.addEventListener("click", function () {
                        removeTag(tagValue, tag, e);
                        changeTableProductDetail()
                    });
                    // Ngăn chặn hành vi mặc định của Enter (ngăn xuống dòng)
                    e.preventDefault();

                    saveProduct()
                    changeTableProductDetail()
                }
            })
        });
        event.target.hidden = true
    }
    if (event.target.classList.contains('delete-button')) {
        // Xóa thẻ cha của nút "Xóa"
        var id = event.target.value;
        if (document.getElementById(id) != null) {
            document.getElementById(id).hidden = false
        }

        var inputGroupDiv = event.target.closest('.attributes');
        if (inputGroupDiv) {
            inputGroupDiv.remove();
        }
        changeTableProductDetail()
    }
    if (event.target.classList.contains('add-variant')) {
        var itemContentDiv = document.querySelector('.item-content-variant');
// Tạo phần tử <div class="attributes">
        var attributesGroupDiv = document.createElement('div');
        attributesGroupDiv.className = 'variant mb-3';

// Tạo label
        var label = document.createElement("label")
        label.className = "col-form-label"
        label.innerText = event.target.value
        attributesGroupDiv.appendChild(label)
        // div input group
        var inputGroupDiv = document.createElement("div")
        inputGroupDiv.className = "input-group shadow-none"

        var inputDiv = document.createElement("div")
        inputDiv.className = "input-div col-sm-10"
        // tạo textarea
        var input = document.createElement("input")
        input.className = "form-control input-data data-variant w-100"
        input.rows = 1
        input.id = "_" + event.target.id
        inputDiv.appendChild(input)
        inputGroupDiv.appendChild(inputDiv)

        var deleteDiv = document.createElement('div');
        deleteDiv.className = "col-sm-2 text-center"
        //tạo button delete
        var buttonDel = document.createElement('button')
        buttonDel.type = 'button';
        buttonDel.className = "btn btn-danger delete-button-variant";
        buttonDel.value = event.target.id
        var spanInBtn = document.createElement('span');
        spanInBtn.className = "tf-icons bx bxs-trash delete-button-variant";
        spanInBtn.value = event.target.id

        buttonDel.appendChild(spanInBtn)
        deleteDiv.appendChild(buttonDel)
        inputGroupDiv.appendChild(deleteDiv)
        attributesGroupDiv.appendChild(inputGroupDiv)
        itemContentDiv.appendChild(attributesGroupDiv);
        $.each($(".input-data"), function (index, item) {
            item.addEventListener("keydown", function (e) {
                if (e.key === "Enter" && e.target.value.trim() !== "") {
                    // Tạo một thẻ span cho tag mới
                    const inputValue = e.target.value.trim();
                    const tagValue = inputValue.slice(0);
                    var listValue = []
                    var allTag = document.querySelectorAll("." + e.target.id)

                    $.each(allTag, function (index, item) {
                        listValue.push(item.textContent)
                    })
                    // thêm tag
                    if (listValue.includes(tagValue)) {
                        // Kiểm tra xem tag đã tồn tại chưa
                        return;
                    }
                    e.target.value = "";

                    const tag = document.createElement("div");
                    tag.textContent = tagValue;
                    tag.classList.add("tag", e.target.id);
                    e.target.closest(".input-div").appendChild(tag);
                    // Xử lý sự kiện khi người dùng nhấp vào tag để xóa

                    tag.addEventListener("click", function () {
                        removeTag(tagValue, tag, e);
                        changeTableProductDetail()
                    });
                    // Ngăn chặn hành vi mặc định của Enter (ngăn xuống dòng)
                    e.preventDefault();
                    saveProduct()
                    changeTableProductDetail()
                }
            })
        });
        event.target.hidden = true
    }
    if (event.target.classList.contains('delete-button-variant')) {
        // Xóa thẻ cha của nút "Xóa"
        var id = event.target.value;
        if (document.getElementById(id) != null) {
            document.getElementById(id).hidden = false
        }
        var inputGroupDiv = event.target.closest('.variant');
        if (inputGroupDiv) {
            inputGroupDiv.remove();
        }
        changeTableProductDetail()
    }
})


function genProduct(listAttri, currentCombination, currentIndex, temp) {
    if (listAttri.length == currentIndex) {
        dataProductDetail.type = $('#select-type option:selected').text();
        var listAttributes = []
        for (let i = 0; i < temp.length; i++) {
            var loop = {
                id: temp[i].id,
                value: currentCombination[i]
            }
            listAttributes.push(loop)
        }
        dataProductDetail.listAttributes.push(listAttributes)
        return
    }

    var listTemp = []
    $.each(listAttri, function (index, item) {
        var temp = {
            id: item.id,
        }
        listTemp.push(temp)
    })

    var currentList = listAttri[currentIndex].value;

    $.each(currentList, function (index, item) {
        currentCombination.push(item)
        genProduct(listAttri, currentCombination, currentIndex + 1, listTemp)
        currentCombination.pop()
    })
}

function changeTableProductDetail() {
    var group = $("#select-group")
    // var type = $("#select-type")
    var listVariant = document.querySelectorAll(".input-data.data-variant")
    var data = {
        group: group.value,
        listAttributes: []
    }
    $.each(listVariant, function (index, item) {
        var arr = []
        var allTag = document.querySelectorAll("." + item.id)
        $.each(allTag, function (index, value) {
            arr.push(value.textContent)
        })
        data.listAttributes.push({
            id: item.id.substring(2),
            value: arr,
        })
    })

    dataProductDetail.listAttributes = []
    genProduct(data.listAttributes, [], 0, [])
    if (dataProductDetail.listAttributes.length !== 0) {
        $("#table-product-detail tbody").empty()
        var list = dataProductDetail.listAttributes
        $.each(list, function (index, item) {
            // lấy tên san phẩm
            if (item.length === 0) {
                return
            }
            var temp = item
            var nameProduct = []
            document.getElementById("select-type")
            $.each(temp, function (index, itemTemp) {
                nameProduct.push(itemTemp.value)
            })
            //end
// Tạo một thẻ <tr>

            const tableRow = document.createElement("tr");

// Tạo một thẻ <td> cho Tên hiển thị
            const displayNameCell = document.createElement("td");
            var nameSpan = document.createElement("span");
            nameSpan.innerText = nameProduct.join(" ");
            nameSpan.className = "name-product"
            displayNameCell.appendChild(nameSpan)
            tableRow.appendChild(displayNameCell);

// Tạo các ô input
            const inputCells = [];
            const name = ["sku", "quantity", "priceImport", "priceExport"]
            for (let i = 0; i < 4; i++) {
                const inputCell = document.createElement("td");
                const input = document.createElement("input");
                input.className = "form-control " + name[i];
                if (i === 0) {
                    input.type = "text";
                } else {
                    input.type = "number";
                    input.value = 0
                }
                input.name = name[i]

                var lable = document.createElement("span")
                lable.className = "fst-italic text-danger " + "error-" + name[i]
                inputCell.appendChild(input);
                inputCell.appendChild(lable);
                inputCells.push(inputCell);
            }
            tableRow.append(...inputCells);

// Tạo một ô chứa ảnh và input file
            const imageCell = document.createElement("td");
            const imageList = document.createElement("ul");
            imageList.className = "list-unstyled users-list avatar-group m-0 d-flex align-items-center";

            for (let i = 0; i < 5; i++) {
                const listItem = document.createElement("li");
                listItem.setAttribute("data-bs-toggle", "tooltip");
                listItem.setAttribute("data-popup", "tooltip-custom");
                listItem.setAttribute("data-bs-placement", "bottom");
                listItem.className = "avatar avatar-xl pull-up border-dark border";
                listItem.setAttribute("data-bs-offset", "0,4");
                listItem.setAttribute("data-bs-html", "true");

                const image = document.createElement("img");
                image.src = "/image/product/tải ảnh lên.jpg";
                image.alt = "Chưa có ảnh";
                image.className = "image-preview";
                image.onclick = clickImage

                const inputFile = document.createElement("input");
                inputFile.type = "file";
                inputFile.hidden = true;
                inputFile.onchange = onchangeImage
                if (i == 0) {
                    listItem.setAttribute("title", "Ảnh chính");
                    inputFile.className = "file-input true" + " imageUpload" + index;
                } else {
                    listItem.setAttribute("title", "Ảnh phụ");
                    inputFile.className = "file-input false" + " imageUpload" + index;
                }
                listItem.appendChild(image);
                listItem.appendChild(inputFile);
                imageList.appendChild(listItem);
            }

            var lable = document.createElement("span")
            lable.className = "fst-italic text-danger " + "error-image"
            imageCell.appendChild(imageList);
            imageCell.appendChild(lable)
            tableRow.appendChild(imageCell);

// Tạo ô cho nút dropdown
            const activeCell = document.createElement("td");

            var check = document.createElement("input")
            check.className = "form-check-input larger-checkbox check-active"
            check.type = "checkbox"
            check.checked=true
            check.value = index

            activeCell.appendChild(check);
            tableRow.appendChild(activeCell);

// Thêm thẻ <tr> vào bảng tail
            const table = document.querySelector(".table-product tbody"); // Điều chỉnh chọn bảng cụ thể
            table.appendChild(tableRow);
            document.getElementById("detail-pro").hidden = false
        });
    } else {
        document.getElementById("detail-pro").hidden = true
    }
}

//remove tag input attribute
function removeTag(tagValue, tagElement, e) {
    e.target.closest(".input-div").removeChild(tagElement);
}


function validate() {
    var check = [];
    var skuCode = document.getElementById("sku-code")
    var listSku = document.getElementsByClassName("sku")
    var listQuantity = document.getElementsByClassName("quantity")
    var listPriceImport = document.getElementsByClassName("priceImport")
    var listPriceExport = document.getElementsByClassName("priceExport")


    $.each(listSku, function (index, item) {
        var parentElement = item.closest('td');
        var span = parentElement.querySelector("span.error-sku")
        if (item.value === "") {
            span.textContent = "Nhập mã sku cho biến thể"
            check.sku = false
        } else {
            check.sku = true
            span.textContent = ""
        }
    })
    $.each(listQuantity, function (index, item) {
        var parentElement = item.closest('td');
        var span = parentElement.querySelector("span.error-quantity")
        if (item.value === "" || Number(item.value) < 0) {
            span.textContent = "Số lượng >=0"
            check.quantity = false
        } else {
            check.quantity = true
            span.textContent = ""
        }
    })
    $.each(listPriceImport, function (index, item) {
        var parentElement = item.closest('td');
        var span = parentElement.querySelector("span.error-priceImport")
        if (item.value === "" || Number(item.value) < 0) {
            span.textContent = "Giá nhập >=0 "
            check.priceImport = false
        } else {
            check.priceImport = true
            span.textContent = ""
        }
    })
    $.each(listPriceExport, function (index, item) {
        var parentElement = item.closest('td');
        var span = parentElement.querySelector("span.error-priceExport")
        if (item.value === "" || Number(item.value) < 0) {
            span.textContent = "Giá bán >=0"
            check.priceExport = false
        } else {
            check.priceExport = true
            span.textContent = ""
        }
    })

    var checkActive = document.getElementsByClassName("check-active")

    $.each(checkActive, function (index, item) {
        var value = item.getAttribute("value")
        var image = "imageUpload" + value
        var listImage = document.getElementsByClassName(image)
        $.each(listImage, function (index, item) {
            var parentElement = item.closest('td');
            var span = parentElement.querySelector("span.error-image")
            // Lấy tệp từ trường chọn tệp
            var fileName = item.value; // Lấy đường dẫn đầy đủ của tệp
            var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
            if (lastIndex >= 0) {
                fileName = fileName.substr(lastIndex + 1);
            }
            if (fileName === "") {
                span.textContent = "Chọn ảnh sản phẩm"
            }
        })

        if (check.sku === false || check.quantity === false || check.priceExport === false || check.priceImport === false) {
            return false
        }
        return true;
    })
}

// clear form modal add product
function clear() {
    loadDataGroup()
    var container = document.querySelector(".item-content")
    while (container.firstChild) {
        container.removeChild(container.firstChild);
    }
    var containerVariant = document.querySelector(".item-content-variant")
    while (containerVariant.firstChild) {
        containerVariant.removeChild(containerVariant.firstChild);
    }
    $(".tag").remove()
    var buttonAdd = document.querySelectorAll(".addAttributes")
    $.each(buttonAdd, function (index, item) {
        item.hidden = false
    })
    var buttonAdd = document.querySelectorAll(".add-variant")
    $.each(buttonAdd, function (index, item) {
        item.hidden = false
    })

    document.getElementById("sku-code").value = ""
    document.getElementById("name-display").value = ""
    document.getElementById("sameProduct").value = ""
}

