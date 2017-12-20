<?php

function handelRequestProxy() {
	$input = file_get_contents("php://input");
	return $input;
}

?>
