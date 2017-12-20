	
	public $return_model_name$ $function_name$($request_model_name$ request) {
		return requestProxy("$function_name$", request, $return_model_name$.class);
	}