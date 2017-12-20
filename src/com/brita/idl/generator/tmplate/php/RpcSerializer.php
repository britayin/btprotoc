<?php

function serializeToData($obj) {
	return json_encode($obj);
}

function deserializeFromData($data) {
	return json_decode($data);
}

?>
