package main.java.com.greendev.pragma.download;

/**
 * This class represents an instance of a download.
 * @author josediaz
 *
 */
public class Download {
		
		/**
		 * The url to download
		 */
		private String url;
		
		/**
		 * location to store the download
		 */
		private String saveLocation;
		
		/**
		 * Indicates the offset days from today
		 */
		private int dateOffset;
		
		/**
		 * Indicates the type of download 
		 */
		private String downloadType;
		
		/**
		 * Default constructor
		 */
		public Download(){
			
		}
		
		/**
		 * Constructs a new download instance 
		 * @param download - reference download used to construct a new download.
		 */
		public Download(Download download){
			this.url = download.getUrl();
			this.saveLocation = download.getSaveLocation();
			this.dateOffset = download.getDateOffset();
			this.downloadType = download.getDownloadType();
		}
		
		@Override
		public String toString() {
			return "Download [url=" + url + ", saveLocation=" + saveLocation
					+ ", dateOffset=" + dateOffset + ", downloadType="
					+ downloadType + "]";
		}
		
		/**
		 * Gets the url download
		 * @return the download url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * Set the url to download
		 * @param url - url to downlaod
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * Gets the location to store the download in
		 * @return a string representation of the download's location
		 */
		public String getSaveLocation() {
			return saveLocation;
		}
		
		/**
		 * Set the location of where to save the download
		 * @param saveLocation 
		 */
		public void setSaveLocation(String saveLocation) {
			this.saveLocation = saveLocation;
		}
		
		/**
		 * Get date offSet
		 * @return date Offset
		 */
		public int getDateOffset() {
			return dateOffset;
		}
		
		/**
		 * Sets the desired date offset
		 * @param dateOffset - represents the offset to subtract to current date
		 */ 
		public void setDateOffset(int dateOffset) {
			this.dateOffset = dateOffset;
		}

		/**
		 * Get the type of download
		 * @return string representation of download type
		 */
		public String getDownloadType() {
			return downloadType;
		}
		/**
		 * Sets the download type.
		 */
		public void setDownloadType(String downloadType) {
			this.downloadType = downloadType;
		}
}
