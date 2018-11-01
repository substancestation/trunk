function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

// @see domain_node_programs_v2.js
function uploadFile() {
	var uploadFileDialog = document.getElementById('uploadFileDialog');
	uploadFileDialog.showModal();
}


$(document).on("click", "#actionUploadFile", function() {
	document.getElementById('uploadFileDialog').showModal();
});
$(document).on("click", "#okUploadFile", function() {
	var form = document.getElementById("upload_form");
	form.submit();
});
$(document).on("click", "#cancelUploadFile", function() {
	// href="javascript:document.getElementById('upload_form').reset();"
	document.getElementById('upload_form').reset();
	document.getElementById('uploadFileDialog').close();
});


$(document).on("click", "#actionCreateDirectory", function() {
	document.getElementById('createDirectoryDialog').showModal();
});
$(document).on("click", "#okCreateDirectory", function() {
	var form = document.getElementById("new_form");
	form.submit();
});
$(document).on("click", "#cancelCreateDirectory", function() {
	document.getElementById('new_form').reset();
	document.getElementById('createDirectoryDialog').close();
});


$(document).on("click", "#actionDeleteFiles", function() {
	document.getElementById('deleteFilesDialog').showModal();
});
$(document).on("click", "#okDeleteFiles", function() {
	var form = document.getElementById("main_list");
	form.submit();
});
$(document).on("click", "#cancelDeleteFiles", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteFilesDialog').close();
});
