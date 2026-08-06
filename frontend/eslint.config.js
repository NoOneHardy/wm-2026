// @ts-check
const eslint = require('@eslint/js')
const tseslint = require('typescript-eslint')
const angular = require('angular-eslint')

module.exports = tseslint.config(
  {
    files: ['**/*.ts'],
    extends: [
      eslint.configs.recommended,
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      'indent': ['error', 2],
      '@typescript-eslint/no-unused-vars': [
        'error',
        {argsIgnorePattern: '^_'}
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'bet',
          style: 'kebab-case',
        },
      ],
      'quotes': [
        'error',
        'single',
        {'allowTemplateLiterals': true}
      ],
      'semi': [
        'error',
        'never'
      ],
      '@angular-eslint/no-input-rename': 'off',
      '@angular-eslint/prefer-on-push-component-change-detection': 'off'
    },
  },
  {
    files: ['**/*.html'],
    extends: [
      ...angular.configs.templateRecommended,
      ...angular.configs.templateAccessibility,
    ],
    rules: {
      '@angular-eslint/template/label-has-associated-control': 'off',
      '@angular-eslint/template/no-autofocus': 'off'
    },
  }
);
