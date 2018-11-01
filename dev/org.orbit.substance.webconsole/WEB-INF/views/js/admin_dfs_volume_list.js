function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

function setElementValue(elementId, elementValue) {
	document.getElementById(elementId).setAttribute('value', elementValue);
}

//-----------------------------------------------------------------------
// Create nodes
//-----------------------------------------------------------------------
$(document).on("click", "#actionAddNode", function() {
	document.getElementById('newNodeDialog').showModal();
});

$(document).on("click", "#okAddNode", function() {
	document.getElementById("new_form").submit();
});

$(document).on("click", "#cancelAddNode", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newNodeDialog').close();
});

//-----------------------------------------------------------------------
// Change node
//-----------------------------------------------------------------------
function changeNode(elementId, dfsVolumeId, name, enabled) {
	document.getElementById("node__elementId").setAttribute('value', elementId);
	document.getElementById("node__dfs_volume_id").setAttribute('value', dfsVolumeId);
	document.getElementById("node__name").setAttribute('value', name);

	if (enabled) {
		document.update_form_name.enabled.value='true';
	} else {
		document.update_form_name.enabled.value='false';
	}

	document.getElementById('changeNodeDialog').showModal();
}

$(document).on("click", "#okChangeNode", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeNode", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changeNodeDialog').close();
});


//-----------------------------------------------------------------------
//Actions on nodes
//-----------------------------------------------------------------------
function onNodeAction(action, formActionUrl) {
	document.getElementById("main_list__action").value = action;

	var form = document.getElementById("main_list");
	form.setAttribute("action", formActionUrl);

	var nodeActionDialogTitleDiv = document.getElementById("nodeActionDialogTitleDiv");
	var nodeActionDialogMessageDiv = document.getElementById("nodeActionDialogMessageDiv");

	var dialogTitle = null;
	var dialogMessage = null;

	if (action == "enable") {
		dialogTitle = "Enable Nodes";
		dialogMessage = "Are you sure you want to enable the nodes?";

	} else if (action == "disable") {
		dialogTitle = "Disable Nodes";
		dialogMessage = "Are you sure you want to disable the nodes?";

	} else {
		dialogTitle = action + " Nodes";
		dialogMessage = "Are you sure you want to '" + action + "' the programs?";
	}

	nodeActionDialogTitleDiv.innerHTML = dialogTitle;
	nodeActionDialogMessageDiv.innerHTML = dialogMessage;

	document.getElementById('nodeActionDialog').showModal();
}

$(document).on("click", "#okNodeAction", function() {
	document.getElementById("main_list").submit();
});

$(document).on("click", "#cancelNodeAction", function() {
	document.getElementById('main_list').reset();
	document.getElementById('nodeActionDialog').close();
});

//-----------------------------------------------------------------------
// Delete nodes
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteNodes", function() {
	document.getElementById('deleteNodesDialog').showModal();
});

$(document).on("click", "#okDeleteNodes", function() {
	var form = document.getElementById("delete_form");

	var checkboxes = document.getElementsByName("elementId");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var elementId = checkboxes[i].value;

			var elementIdField = document.createElement("input");
			elementIdField.setAttribute("type", "hidden");
			elementIdField.setAttribute("name", "elementId");
			elementIdField.setAttribute("value", elementId);
			form.appendChild(elementIdField);
		}
	}

	form.submit();
	document.getElementById('deleteNodesDialog').close();
});

$(document).on("click", "#cancelDeleteNodes", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteNodesDialog').close();
});
