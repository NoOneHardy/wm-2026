import eslint from '@eslint/js'
import tslint from 'typescript-eslint'
import stylistic from '@stylistic/eslint-plugin'

export default tslint.config(
    eslint.configs.recommended,
    tslint.configs.recommended,
        tslint.configs.stylistic,
    {
        plugins: {
            '@stylistic': stylistic
        },
        rules: {
            'no-var': 'error',
            '@typescript-eslint/no-require-imports': 'off',
            '@typescript-eslint/no-inferrable-types': 'off',
            '@stylistic/indent': ['error', 2],
            '@stylistic/semi': ['error', 'never'],
            '@stylistic/quotes': ['error', 'single']
        }
    }
)