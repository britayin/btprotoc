	
	public $return_model_name$ $function_name$($request_parameters$) {
		
		try {
			
			$request_model_name$ request = $request_model_name$.class.newInstance();
			$set_request_model_values$
			
			return requestProxy("$function_name$", request, $return_model_name$.class);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}