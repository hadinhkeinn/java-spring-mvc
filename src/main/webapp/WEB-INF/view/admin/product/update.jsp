<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="Hỏi Dân IT - Dự án laptopshop" />
                <meta name="author" content="Hỏi Dân IT" />
                <title>Dashboard - Hỏi Dân IT</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script>
                    $(document).ready(() => {
                        const productImg = $("#productImg");
                        const orgImg = "${updatedProduct.image}";
                        console.log(orgImg);
                        if (orgImg) {
                            const urlImage = "/images/product/" + orgImg;
                            $("#imgPreview").attr("src", urlImage);
                            $("#imgPreview").css({ "display": "block" });
                        }
                        productImg.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#imgPreview").attr("src", imgURL);
                            $("#imgPreview").css({ "display": "block" });
                        });
                    });
                </script>

            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />

                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Product</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item">Products</li>
                                    <li class="breadcrumb-item active">Update a Product</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h3>Update a product</h3>
                                            <hr>
                                            <form:form class="row" method="post" enctype="multipart/form-data"
                                                action="/admin/product/update" modelAttribute="updatedProduct">
                                                <div class="mb-3" style="display: none;">
                                                    <label class="form-label">Id:</label>
                                                    <form:input path="id" type="text" class="form-control" />
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <c:set var="nameHasBindError">
                                                        <form:errors path="name" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Name:</label>
                                                    <form:input path="name" type="text"
                                                        class="form-control ${not empty nameHasBindError? 'is-invalid':''}" />
                                                    ${nameHasBindError}
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <c:set var="priceHasBindError">
                                                        <form:errors path="price" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Price:</label>
                                                    <form:input path="price" type="number"
                                                        class="form-control ${not empty priceHasBindError? 'is-invalid':''}" />
                                                    ${priceHasBindError}
                                                </div>

                                                <div class="mb-3">
                                                    <c:set var="detailDescHasBindError">
                                                        <form:errors path="detailDesc" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Detail description:</label>
                                                    <form:textarea path="detailDesc" type="text"
                                                        class="form-control ${not empty detailDescHasBindError? 'is-invalid':''}" />
                                                    ${detailDescHasBindError}
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <c:set var="shortDescHasBindError">
                                                        <form:errors path="shortDesc" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Short description:</label>
                                                    <form:input path="shortDesc" type="text"
                                                        class="form-control ${not empty detailDescHasBindError? 'is-invalid':''}" />
                                                    ${shortDescHasBindError}
                                                </div>

                                                <div class="mb-3 col-12 col-md-6">
                                                    <c:set var="quantityHasBindError">
                                                        <form:errors path="quantity" cssClass="invalid-feedback" />
                                                    </c:set>
                                                    <label class="form-label">Quantity:</label>
                                                    <form:input path="quantity" type="number"
                                                        class="form-control ${not empty quantityHasBindError? 'is-invalid':''}" />
                                                    ${quantityHasBindError}
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <label for="formFile" class="form-label">Factory:</label>
                                                    <form:select class="form-select" path="factory">
                                                        <form:option value="APPLE">Apple</form:option>
                                                        <form:option value="SAMSUNG">Samsung</form:option>
                                                        <form:option value="ASUS">Asus</form:option>
                                                        <form:option value="LENOVO">Lenovo</form:option>
                                                        <form:option value="DELL">Dell</form:option>
                                                        <form:option value="LG">LG</form:option>
                                                        <form:option value="ACER">Acer</form:option>
                                                    </form:select>
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <label for="formFile" class="form-label">Target:</label>
                                                    <form:select class="form-select" path="target">
                                                        <form:option value="GAMING">Gaming</form:option>
                                                        <form:option value="SINHVIEN-VANPHONG">Sinh viên - Văn phòng
                                                        </form:option>
                                                        <form:option value="THIET-KE-DO-HOA">Thiết kế đồ hoạ
                                                        </form:option>
                                                        <form:option value="MONG-NHE">Mỏng nhẹ</form:option>
                                                        <form:option value="DOANH-NHAN">Doanh nhân</form:option>
                                                    </form:select>
                                                </div>
                                                <div class="mb-3 col-12 col-md-6">
                                                    <label for="productImg" class="form-label">Image:</label>
                                                    <input class="form-control" type="file" name="productImg"
                                                        id="productImg" accept=".png, .jpg, .jpeg">
                                                </div>
                                                <div class="col-12 mb-3">
                                                    <img alt="Product Image Preview"
                                                        style="max-height: 250px; display: none;" id="imgPreview">
                                                </div>
                                                <button type="submit" class="btn btn-primary">Save</button>
                                            </form:form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>