package com.enquero.api.driscolls.APICommon;

//==================================
public class


PojoUtility {

    public static class Project {
        private String id;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }

//==================================

    public static class Issuetype {
        private String id;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }

//==================================

    public static class Fields {
        private Project project;

        private String summary;

        private String description;

        private Issuetype issuetype;

        public void setProject(Project project) {
            this.project = project;
        }

        public Project getProject() {
            return this.project;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getSummary() {
            return this.summary;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public void setIssuetype(Issuetype issuetype) {
            this.issuetype = issuetype;
        }

        public Issuetype getIssuetype() {
            return this.issuetype;
        }
    }

//==================================

    public static class Root {
        private Fields fields;

        public void setFields(Fields fields) {
            this.fields = fields;
        }

        public Fields getFields() {
            return this.fields;
        }
    }
}



