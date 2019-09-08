package hudson.plugins.jira.extension;

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueFieldId;
import com.atlassian.jira.rest.client.internal.json.BasicIssueJsonParser;
import com.atlassian.jira.rest.client.internal.json.BasicPriorityJsonParser;
import com.atlassian.jira.rest.client.internal.json.BasicProjectJsonParser;
import com.atlassian.jira.rest.client.internal.json.IssueTypeJsonParser;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.JsonParseUtil;
import com.atlassian.jira.rest.client.internal.json.UserJsonParser;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Merav Yaakov
 */

public class IssueTestParser implements JsonObjectParser<Issue> {

    private static Set<String> SPECIAL_FIELDS = Sets.newHashSet(IssueFieldId.ids());
    private final BasicIssueJsonParser basicIssueJsonParser = new BasicIssueJsonParser();
    private final IssueTypeJsonParser issueTypeJsonParser = new IssueTypeJsonParser();
    private final BasicPriorityJsonParser priorityJsonParser = new BasicPriorityJsonParser();
    private final UserJsonParser userJsonParser = new UserJsonParser();
    private final BasicProjectJsonParser projectJsonParser = new BasicProjectJsonParser();




    private final JSONObject providedNames;
    private final JSONObject providedSchema;

    public IssueTestParser() {
        this.providedNames = null;
        this.providedSchema = null;
    }

    public IssueTestParser(JSONObject providedNames, JSONObject providedSchema) {
        this.providedNames = providedNames;
        this.providedSchema = providedSchema;
    }

    @Override
    public Issue parse(JSONObject issueJson) throws JSONException {
        BasicIssue basicIssue = this.basicIssueJsonParser.parse(issueJson);
        //String summary = this.getFieldStringValue(issueJson, IssueFieldId.SUMMARY_FIELD.id);
        //String description = this.getOptionalFieldStringUnisex(issueJson, IssueFieldId.DESCRIPTION_FIELD.id);
        //Collection<IssueField> fields = this.parseFields(issueJson);
        //IssueType issueType = this.issueTypeJsonParser.parse(this.getFieldUnisex(issueJson, IssueFieldId.ISSUE_TYPE_FIELD.id));
        ////BasicPriority
        ////    priority = (BasicPriority)this.getOptionalNestedField(issueJson, IssueFieldId.PRIORITY_FIELD.id, this.priorityJsonParser);
        //BasicProject project = this.projectJsonParser.parse(this.getFieldUnisex(issueJson, IssueFieldId.PROJECT_FIELD.id));
        ////Collection<IssueLink> issueLinks = this.parseOptionalArray(issueJson, new JsonWeakParserForJsonObject(this.issueLinkJsonParserV5), "fields", IssueFieldId.LINKS_FIELD.id);
        return new MyIssue(basicIssue.getSelf(), basicIssue.getKey(), basicIssue.getId());
    }

    private String getFieldStringValue(JSONObject json, String attributeName) throws JSONException {
        JSONObject fieldsJson = json.getJSONObject("fields");
        Object summaryObject = fieldsJson.get(attributeName);
        if (summaryObject instanceof JSONObject) {
            return ((JSONObject)summaryObject).getString("value");
        } else if (summaryObject instanceof String) {
            return (String)summaryObject;
        } else {
            throw new JSONException("Cannot parse [" + attributeName + "] from available fields");
        }
    }

    @Nullable
    private String getOptionalFieldStringUnisex(JSONObject json, String attributeName) throws JSONException {
        JSONObject fieldsJson = json.getJSONObject("fields");
        return JsonParseUtil.getOptionalString(fieldsJson, attributeName);
    }

    private Collection<IssueField> parseFields(JSONObject issueJson) throws JSONException {
        JSONObject names = this.providedNames != null ? this.providedNames : issueJson.optJSONObject("names");
        Map<String, String> namesMap = this.parseNames(names);
        JSONObject schema = this.providedSchema != null ? this.providedSchema : issueJson.optJSONObject("schema");
        Map<String, String> typesMap = this.parseSchema(schema);
        JSONObject json = issueJson.getJSONObject("fields");
        ArrayList<IssueField> res = new ArrayList(json.length());
        Iterator iterator = json.keys();

        while(iterator.hasNext()) {
            String key = (String)iterator.next();

            try {
                if (!SPECIAL_FIELDS.contains(key)) {
                    Object value = json.opt(key);
                    res.add(new IssueField(key, (String)namesMap.get(key), (String)typesMap.get("key"), value != JSONObject.NULL ? value : null));
                }
            } catch (final Exception var11) {
                throw new JSONException("Error while parsing [" + key + "] field: " + var11.getMessage()) {
                    public Throwable getCause() {
                        return var11;
                    }
                };
            }
        }

        return res;
    }

    private Map<String, String> parseNames(JSONObject json) throws JSONException {
        HashMap<String, String> res = Maps.newHashMap();
        Iterator iterator = JsonParseUtil.getStringKeys(json);

        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            res.put(key, json.getString(key));
        }

        return res;
    }

    private Map<String, String> parseSchema(JSONObject json) throws JSONException {
        HashMap<String, String> res = Maps.newHashMap();
        Iterator it = JsonParseUtil.getStringKeys(json);

        while(it.hasNext()) {
            String fieldId = (String)it.next();
            JSONObject fieldDefinition = json.getJSONObject(fieldId);
            res.put(fieldId, fieldDefinition.getString("type"));
        }

        return res;
    }

    private JSONObject getFieldUnisex(JSONObject json, String attributeName) throws JSONException {
        JSONObject fieldsJson = json.getJSONObject("fields");
        JSONObject fieldJson = fieldsJson.getJSONObject(attributeName);
        return fieldJson.has("value") ? fieldJson.getJSONObject("value") : fieldJson;
    }

    @Nullable
    private <T> T getOptionalNestedField(JSONObject s, String fieldId, JsonObjectParser<T> jsonParser) throws JSONException {
        JSONObject fieldJson = JsonParseUtil.getNestedOptionalObject(s, new String[]{"fields", fieldId});
        return fieldJson != null ? jsonParser.parse(fieldJson) : null;
    }

    class MyIssue extends Issue {

        public MyIssue(URI self, String key, Long id) {
            super(null, self, key, id, null, null, null, null, null, null, null,
                  null,
                  null, null, null, null, null, null, null, null,
                  null, null, null, null, null, null, null, null, null,
                  null,
                  null, null);
        }
    }

}
