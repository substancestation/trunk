// @see domain_node_programs_v2.js
function uploadFile() {
	var uploadFileDialog = document.getElementById('uploadFileDialog');
	uploadFileDialog.showModal();
}

$(document).on("click", "#actionUploadFile", function() {
	document.getElementById('uploadFileDialog').showModal();
});

$(document).on("click", "#cancelUploadFile", function() {
	// href="javascript:document.getElementById('upload_form').reset();"
	document.getElementById('upload_form').reset();
	document.getElementById('uploadFileDialog').close();
});
