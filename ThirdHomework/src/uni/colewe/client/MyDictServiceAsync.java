package uni.colewe.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import uni.colewe.shared.MyDictionaryEntry;

public interface MyDictServiceAsync {
	void queryServer(String query, boolean rev, boolean like, AsyncCallback<List<MyDictionaryEntry>> callback) throws IllegalArgumentException;
}
