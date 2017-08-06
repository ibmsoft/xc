package com.cubedrive.base.event;

import com.cubedrive.base.domain.document.DocumentFile;

public class DocumentDeleteEvent {

	private final DocumentFile documentFile;

	public DocumentDeleteEvent(DocumentFile documentFile) {
		this.documentFile = documentFile;
	}

	public DocumentFile getDocumentFile() {
		return documentFile;
	}
}
