// Parse the MB_RESPONSE environment variable
const request = JSON.parse(process.env.MB_REQUEST);
const response = JSON.parse(process.env.MB_RESPONSE);

// Access the data object from the response body
const data = response.body.data;

const pathParts = request.path.split('/');
if (pathParts.length > 2) {
    const id = parseInt(pathParts[pathParts.length - 1]);
    data.id = id;
}

// Convert string values to integers
if (data.aisle_number) {
    data.aisle_number = parseInt(data.aisle_number);
}
if (data.author && data.author.id) {
    data.author.id = parseInt(data.author.id);
}
// Output the modified response
console.log(JSON.stringify(response));