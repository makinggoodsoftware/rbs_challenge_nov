package entities;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.InstanceCreator;
import com.google.gson.annotations.SerializedName;

public class EntryInstanceCreator implements InstanceCreator<Entry<String,Card>>{

	public Entry<String, Card> createInstance(Type type) {

		return new Entry<String, Card>() {
			@SerializedName("Key")
			private String key;
			@SerializedName("Value")
			private Card value;
			public Card setValue(Card value) {
				Card oldValue = this.value;
				this.value = value;
				return oldValue;
			}
			
			public Card getValue() {
				return this.value;
			}
			
			public String getKey() {

				return key;
			}
		};
	}
	
}
