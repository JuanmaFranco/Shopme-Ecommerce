
$(document).ready(function () {

    // Cancel Button -> Back to home page
    $("#cancelButton").on("click", function () {
        window.location = moduleURL;
    });

    // Image upload
    $("#fileImage").change(function () {
        let fileSize = this.files[0].size;
        if (fileSize > 1048576) {  // 1048576 = 1024 x 1024 = 1MB
            this.setCustomValidity("You must choose an image less than 1MB");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

// Show image thumbnail
function showImageThumbnail(fileInput) {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    }

    reader.readAsDataURL(file);
}