// HTML에서 id가 "ratingForm"인 form을 가져옴
const form = document.getElementById("ratingForm");

// HTML에서 id가 "result"인 영역을 가져옴
// 성공/실패 메시지를 여기에 표시함
const result = document.getElementById("result");


// form에서 "submit" 이벤트가 발생했을 때 실행
form.addEventListener("submit", async function (event) {

    // form을 제출할 때 페이지가 새로고침되는 것을 막음
    event.preventDefault();


    // HTML에서 사용자가 입력한 값 가져오기

    // 평점
    const rating = document.getElementById("rating").value;

    // 이름
    const name = document.getElementById("name").value;

    // 댓글 내용
    const content = document.getElementById("content").value;

    // 비밀번호
    const password = document.getElementById("password").value;


    // Spring Boot로 보낼 데이터를 객체로 만듦
    const data = {
        // HTML에서 가져온 문자열을 숫자로 변환
        rating: Number(rating),

        // 이름
        name: name,

        // 댓글
        content: content,

        // 비밀번호
        password: Number(password)
    };


    try {

        const response = await fetch("/guestbook/post", {

            // 데이터를 저장하는 요청이므로 POST 사용
            method: "POST",

            // JSON 형식으로 데이터를 보낸다고 알려줌
            headers: {
                "Content-Type": "application/json"
            },

            // JavaScript 객체를 JSON 문자열로 변환해서 전송
            body: JSON.stringify(data)
        });


        // 서버에서 정상적인 응답을 받았는지 확인
        if (response.ok) {

            // 성공 메시지 출력
            result.textContent = "등록 성공!";

            // 입력했던 내용을 전부 비움
            form.reset();

        } else {

            // 서버가 오류 상태 코드를 보냈을 때
            // 서버에서 보낸 오류 내용을 가져옴
            const errorText = await response.text();

            // 개발자가 오류를 확인할 수 있도록 콘솔에 출력
            console.log("HTTP 상태 코드:", response.status);
            console.log("서버 응답:", errorText);

            // 화면에 실패 메시지 출력
            result.textContent =
                "등록 실패! 상태 코드: " + response.status;
        }

    } catch (error) {

        // 서버 자체에 연결하지 못했을 때 실행
        console.error("서버 연결 오류:", error);

        // 사용자에게 메시지 표시
        result.textContent = "서버 연결 실패!";
    }
});
