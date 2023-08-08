package site.bidderown.server.base.parser;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringELParser {
    private CustomSpringELParser() {}

    /**
     * @param parameterNames 메서드의 파라미터 이름 e.g.) {"a", "b"}
     * @param args 메서드의 실제 값 e.g.) 10, 20
     * @param key 넘어온 값 중 파싱하고 싶은 표현식 e.g.) "#a + #b"
     * @return 표현식의 값 e.g.) 30
     */
    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
