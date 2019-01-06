package uni.colewe.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import uni.colewe.shared.MyDictionaryEntry;

@RemoteServiceRelativePath("dict")
public interface MyDictService extends RemoteService {
	List<MyDictionaryEntry> queryServer(String query, boolean rev, boolean like) throws IllegalArgumentException;
}
