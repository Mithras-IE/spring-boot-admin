/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import SbaButton from './sba-button.vue';

export default {
  component: SbaButton,
  title: 'SBA Components/Button',
};

const Template = (args) => {
  return {
    components: {SbaButton},
    setup() { return { args }; },
    template: `<sba-button v-bind="args">${args.label}</sba-button>`,
  }
};

export const DefaultButton = Template.bind({});
DefaultButton.args = {
  label: 'Default button',
};

export const PrimaryButton = Template.bind({});
PrimaryButton.args = {
  label: 'Primary button',
  'type': 'primary'
};

export const DangerButton = Template.bind({});
DangerButton.args = {
  label: 'Danger button',
  'type': 'danger'
};
