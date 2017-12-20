<?php
header("Content-Type:text/json;charset=UTF-8");

require 'RpcProxy.php';
require 'RpcSerializer.php';
require 'impl/$service_name$.php';

$input = handelRequestProxy();
$requestObj = deserializeFromData($input);
echo serializeToData($service_name$::$function_name$($request_parameters$));
?>