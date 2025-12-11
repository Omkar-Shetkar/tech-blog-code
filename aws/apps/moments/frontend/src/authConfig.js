const region = "us-east-1";
const userPoolDomain = "us-east-1c5qlsahgg.auth.us-east-1.amazoncognito.com"; // your domain
const clientId = "61v1ocqaqmbqefe7oi2046sijq";
const redirectUri =
  window.location.origin; // works for localhost and CloudFront

const cognitoAuthUrl = `https://${userPoolDomain}/oauth2/authorize` +
  `?client_id=${encodeURIComponent(clientId)}` +
  `&response_type=code` +
  `&scope=${encodeURIComponent("openid email profile")}` +
  `&redirect_uri=${encodeURIComponent(redirectUri)}`;

const tokenUrl = `https://${userPoolDomain}/oauth2/token`;

const logoutUrl =
  `https://${userPoolDomain}/logout` +
  `?client_id=${encodeURIComponent(clientId)}` +
  `&logout_uri=${encodeURIComponent(redirectUri)}`;

export { region, clientId, redirectUri, cognitoAuthUrl, tokenUrl, logoutUrl };
