/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.restdocs.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.springframework.restdocs.templates.TemplateFormat;
import org.springframework.util.FileCopyUtils;

/**
 * {@link Matcher Matchers} for verify the contents of generated documentation snippets.
 *
 * @author Andy Wilkinson
 */
public final class SnippetMatchers {

	private SnippetMatchers() {

	}

	public static SnippetMatcher snippet(TemplateFormat templateFormat) {
		return new SnippetMatcher(templateFormat);
	}

	/**
	 * A {@link Matcher} for a snippet file.
	 */
	public static final class SnippetMatcher extends BaseMatcher<File> {

		private final TemplateFormat templateFormat;

		private Matcher<String> expectedContents;

		private SnippetMatcher(TemplateFormat templateFormat) {
			this.templateFormat = templateFormat;
		}

		@Override
		public boolean matches(Object item) {
			if (snippetFileExists(item)) {
				if (this.expectedContents != null) {
					try {
						return this.expectedContents.matches(read((File) item));
					}
					catch (IOException e) {
						return false;
					}
				}
				return true;
			}
			return false;
		}

		private boolean snippetFileExists(Object item) {
			return item instanceof File && ((File) item).isFile();
		}

		private String read(File snippetFile) throws IOException {
			return FileCopyUtils.copyToString(
					new InputStreamReader(new FileInputStream(snippetFile), "UTF-8"));
		}

		@Override
		public void describeMismatch(Object item, Description description) {
			if (!snippetFileExists(item)) {
				description.appendText("The file " + item + " does not exist");
			}
			else if (this.expectedContents != null) {
				try {
					this.expectedContents.describeMismatch(read((File) item),
							description);
				}
				catch (IOException e) {
					description
							.appendText("The contents of " + item + " cound not be read");
				}
			}
		}

		@Override
		public void describeTo(Description description) {
			if (this.expectedContents != null) {
				this.expectedContents.describeTo(description);
			}
			else {
				description
						.appendText(this.templateFormat.getFileExtension() + " snippet");
			}
		}

		public SnippetMatcher withContents(Matcher<String> matcher) {
			this.expectedContents = matcher;
			return this;
		}

	}

}
