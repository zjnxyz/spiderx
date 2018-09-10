package app.bravo.zu.spiderx.core.parser.selector;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Object contains regex results.<br>
 * For multi group result extension.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @author tony
 */
@NoArgsConstructor
@AllArgsConstructor
class RegexResult {

    private String[] groups;

    public static final RegexResult EMPTY_RESULT = new RegexResult();

    public String get(int groupId) {
        if (groups == null) {
            return null;
        }
        return groups[groupId];
    }

}
