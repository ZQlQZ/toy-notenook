// 统一服务导出文件
export { config, errorCodes, dataStructures } from './config';
export { authUtils, aiUtils, httpUtils, validationUtils } from './utils';
export { default as authService } from './authService';
export { default as noteService } from './noteService';
export { default as aiService } from './aiService';
export { http, default as httpClient } from './httpClient';

// 默认导出所有服务
export default {
  config: () => import('./config'),
  utils: () => import('./utils'),
  authService: () => import('./authService'),
  noteService: () => import('./noteService'),
  aiService: () => import('./aiService'),
  httpClient: () => import('./httpClient')
};