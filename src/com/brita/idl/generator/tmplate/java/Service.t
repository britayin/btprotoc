package $java_package$;

import com.brita.rpc.CommonRpc;
import com.brita.rpc.proxy.RpcProxy;
import com.brita.rpc.serialize.RpcSerializer;

public class $service_name$ extends CommonRpc{

	public $service_name$(RpcSerializer rpcSerializer, RpcProxy rpcProxy) {
		super(rpcSerializer, rpcProxy);
	}

	$functions$
	
	
	@Override
	public String getDomain() {
		return "$domain$";
	}
	
	@Override
	public String getServiceName() {
		return "$service_name$";
	}
}